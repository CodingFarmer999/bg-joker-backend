package com.bg.joker.controller;

import com.bg.joker.dto.RegisterRequest;
import com.bg.joker.entity.User;
import com.bg.joker.repository.UserRepository;
import com.bg.joker.security.CustomUserDetails;
import com.bg.joker.security.JwtUtils;
import com.bg.joker.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtils jwtUtils, UserRepository userRepository,
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully: " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        // the "username" parameter might be an email address or a regular username
        User user = userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByUsername(username).orElse(null));

        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            String token = jwtUtils.generateToken(new CustomUserDetails(user));
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid email/username or password");
    }

    /**
     * Endpoint to validate the provided token.
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        try {
            String extractedUsername = jwtUtils.extractUsername(token);

            return userRepository.findByUsername(extractedUsername)
                    .map(u -> {
                        Map<String, Object> response = new HashMap<>();
                        response.put("valid", true);
                        response.put("username", u.getUsername());
                        response.put("displayName", u.getDisplayName());
                        response.put("role", u.getRole());
                        return ResponseEntity.ok((Object) response);
                    })
                    .orElse(ResponseEntity.status(401).body((Object) "User not found"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid token: " + e.getMessage());
        }
    }
}
