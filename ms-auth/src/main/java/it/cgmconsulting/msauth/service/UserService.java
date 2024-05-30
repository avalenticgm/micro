package it.cgmconsulting.msauth.service;

import it.cgmconsulting.msauth.entity.Role;
import it.cgmconsulting.msauth.entity.User;
import it.cgmconsulting.msauth.exception.GenericException;
import it.cgmconsulting.msauth.exception.ResourceNotFoundException;
import it.cgmconsulting.msauth.payload.request.SigninRequest;
import it.cgmconsulting.msauth.payload.request.SignupRequest;
import it.cgmconsulting.msauth.payload.response.SigninResponse;
import it.cgmconsulting.msauth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

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
            return ResponseEntity.ok("Role has been updated for user "+user.getUsername());
        }
        return ResponseEntity.status(400).body("Same role");
    }

    public String getUsername(int id){
        String username = repo.getUsername(id);
        return username != null ? username : "anonymous";
    }


}
