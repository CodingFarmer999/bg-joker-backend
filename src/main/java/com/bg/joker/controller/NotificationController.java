package com.bg.joker.controller;

import com.bg.joker.entity.Notification;
import com.bg.joker.entity.User;
import com.bg.joker.security.JwtUtils;
import com.bg.joker.service.NotificationService;
import com.bg.joker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    private User getAuthenticatedUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        String username = jwtUtils.extractUsername(token);
        return userService.findByUsername(username)
                .orElseGet(() -> userService.findByEmail(username).orElse(null));
    }

    @GetMapping
    public ResponseEntity<?> getUserNotifications(@RequestHeader("Authorization") String authHeader) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        List<Notification> notifications = notificationService.getUserNotifications(user.getId());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount(@RequestHeader("Authorization") String authHeader) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        long count = notificationService.getUnreadCount(user.getId());
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        User user = getAuthenticatedUser(authHeader);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok().build();
    }
}
