package it.cgmconsulting.msauth.service;

import it.cgmconsulting.msauth.entity.Role;
import it.cgmconsulting.msauth.entity.User;
import it.cgmconsulting.msauth.exception.GenericException;
import it.cgmconsulting.msauth.exception.ResourceNotFoundException;
import it.cgmconsulting.msauth.payload.request.SigninRequest;
import it.cgmconsulting.msauth.payload.request.SignupRequest;
import it.cgmconsulting.msauth.payload.response.SigninResponse;
import it.cgmconsulting.msauth.payload.response.SimpleUserResponse;
import it.cgmconsulting.msauth.repository.UserRepository;
import it.cgmconsulting.msauth.utils.Consts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${application.security.internalToken}")
    String internalToken;

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<?> signup(SignupRequest request) {
        if(repo.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            throw new GenericException("Username or email already in use", HttpStatus.CONFLICT);

        User user = User.builder()
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .email(request.getEmail())
                .role(Role.MEMBER)
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();
        repo.save(user);
        log.info("Signup : {} " , user );
        return ResponseEntity.status(201).body(user);
    }

    public ResponseEntity<?> signin(SigninRequest request) {
        User user = repo.findByUsername(request.getUsername())
                .orElseThrow(()-> new GenericException("Bad credentials", HttpStatus.BAD_REQUEST));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new GenericException("Bad credentials", HttpStatus.BAD_REQUEST);

        if(!user.isEnabled())
            throw new GenericException("Account disabled", HttpStatus.FORBIDDEN);

        String jwt = jwtService.generateToken(user);
        SigninResponse response = new SigninResponse(user.getUsername(), user.getRole().name(), jwt);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> changeRole(int userId, String role, int id) {
        User user = repo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));

        if(id == user.getId())
            throw new GenericException("You cannot change role to yourself", HttpStatus.FORBIDDEN);

        if(!user.getRole().name().equals(role)) {
            user.setRole(Role.valueOf(role.toUpperCase()));
            user.setUpdatedAt(LocalDateTime.now());
           /* if(role.equals("WRITER")){
                // aggiorno mappa getWiters su ms-post
                ResponseEntity<?> r = sendNeWriter(user.getId(), user.getUsername());
                if(r.getStatusCode() != HttpStatus.OK)
                    return ResponseEntity.status(503).body("Change role to WRITER failed");
            }
            */
            return ResponseEntity.ok("Role has been updated for user "+user.getUsername());
        }
        return ResponseEntity.status(400).body("Same role");
    }

    public String getUsername(int id){
        String username = repo.getUsername(id);
        return username != null ? username : "anonymous";
    }


    public Map<Integer, String> getUsernames(Set<Integer> authorIds) {
        Set<SimpleUserResponse> set = repo.getSimpleUsers(authorIds);
        Map<Integer, String> map = set.stream().collect(Collectors.toMap(SimpleUserResponse::getId, SimpleUserResponse::getUsername));
        return map;
    }

    private ResponseEntity<?> sendNeWriter(int userId, String username){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY+"/"+Consts.MS_POST+"/v99/writers?id={userId}&username={username}";

        ResponseEntity<String> r = null;
        try{
            r = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class, userId, username);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return r;
    }
}
