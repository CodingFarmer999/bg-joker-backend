package com.bg.joker.controller;

import com.bg.joker.dto.FcmTokenRequest;
import com.bg.joker.entity.User;
import com.bg.joker.security.JwtUtils;
import com.bg.joker.service.UserDeviceTokenService;
import com.bg.joker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FcmTokenController {

    private final UserDeviceTokenService tokenService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/fcm-token")
    public ResponseEntity<?> saveFcmToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody FcmTokenRequest request) {

        log.info("Received request to save FCM token");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.substring(7);
        try {
            String extractedUsername = jwtUtils.extractUsername(token);
            User user = userService.findByUsername(extractedUsername)
                    .orElseGet(() -> userService.findByEmail(extractedUsername).orElse(null));

            if (user == null) {
                log.warn("User not found for username/email: {}", extractedUsername);
                return ResponseEntity.status(404).body("User not found");
            }

            log.info("Extracted username from JWT: {}, Resolved User ID: {}", extractedUsername, user.getId());

            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Token cannot be empty");
            }

            tokenService.upsertUserDeviceToken(user.getId(), request.getToken(), request.getDeviceType());
            return ResponseEntity.ok("Token saved successfully");

        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid Token");
        }
    }
}
