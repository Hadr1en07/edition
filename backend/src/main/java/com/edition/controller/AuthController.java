package com.edition.controller;

import com.edition.dto.AuthResponse;
import com.edition.dto.LoginRequest;
import com.edition.dto.RegisterRequest;
import com.edition.dto.UserResponse;
import com.edition.model.User;
import com.edition.security.JwtUtils;
import com.edition.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            String token = jwtUtils.generateToken(request.getUsername());
            User user = userService.findByUsername(request.getUsername());

            return ResponseEntity.ok(new AuthResponse(
                    token,
                    user.getUsername(),
                    user.getRole().name()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Identifiants invalides"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username déjà pris"));
        }
        User created = userService.createUser(request);
        return ResponseEntity.ok(new UserResponse(created.getUsername(), created.getRole().name()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(new UserResponse(user.getUsername(), user.getRole().name()));
    }
}
