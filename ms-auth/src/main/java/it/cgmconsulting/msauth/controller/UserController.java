package it.cgmconsulting.msauth.controller;

import it.cgmconsulting.msauth.payload.request.SigninRequest;
import it.cgmconsulting.msauth.payload.request.SignupRequest;
import it.cgmconsulting.msauth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/v0/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request){
        return service.signup(request);
    }

    @PostMapping("/v0/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid SigninRequest request){
        return service.signin(request);
    }

    @PutMapping("/v1/{userId}")
    public ResponseEntity<?> changeRole(@PathVariable int userId, @RequestParam String role, @RequestHeader("userId") int id){
        return service.changeRole(userId, role, id);
    }
}
