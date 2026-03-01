package com.bg.joker.controller;

import com.bg.joker.dto.FcmTestRequest;
import com.bg.joker.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/fcm")
public class FcmTestController {

    @Autowired
    private FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> sendTestNotification(@RequestBody FcmTestRequest request) {
        if (request.getToken() == null || request.getToken().isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required");
        }

        String response = fcmService.sendNotification(request.getToken(), request.getTitle(), request.getBody());
        return ResponseEntity.ok(response);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.bg.joker.service.NotificationService notificationService;

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcastNotification(@RequestBody FcmTestRequest request) {
        String response = notificationService.createAndSendBroadcast(request.getTitle(), request.getBody());
        return ResponseEntity.ok(response);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.bg.joker.security.JwtUtils jwtUtils;

    @org.springframework.beans.factory.annotation.Autowired
    private com.bg.joker.service.UserService userService;

    @org.springframework.web.bind.annotation.GetMapping("/whoami")
    public ResponseEntity<?> whoami(
            @org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing token");
        }
        try {
            String token = authHeader.substring(7);
            String username = jwtUtils.extractUsername(token);
            com.bg.joker.entity.User user = userService.findByUsername(username)
                    .orElseGet(() -> userService.findByEmail(username).orElse(null));

            if (user == null) {
                return ResponseEntity.ok("Token subject: " + username + " (User Entity NOT FOUND)");
            }
            return ResponseEntity.ok("Token subject: " + username + ", DB Resolved User ID: " + user.getId()
                    + ", DB Username: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
