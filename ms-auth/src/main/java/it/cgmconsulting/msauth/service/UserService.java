package it.cgmconsulting.msauth.service;

import it.cgmconsulting.msauth.config.AppConfig;
import it.cgmconsulting.msauth.entity.Role;
import it.cgmconsulting.msauth.entity.User;
import it.cgmconsulting.msauth.exception.GenericException;
import it.cgmconsulting.msauth.payload.request.SignupRequest;
import it.cgmconsulting.msauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(SignupRequest request) {
        if(repo.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            throw new GenericException("Username or email already in use");

        User user = User.builder()
                .enabled(true)
                .email(request.getEmail())
                .role(Role.MEMBER)
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();

        repo.save(user);
        return ResponseEntity.status(201).body(user);
    }


}
