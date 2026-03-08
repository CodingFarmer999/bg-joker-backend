package com.bg.joker.controller;

import com.bg.joker.dto.CreateAdminRequest;
import com.bg.joker.entity.User;
import com.bg.joker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Endpoints here should be protected by security configuration to only allow
    // ROLE_ADMIN
    // We add @PreAuthorize here as well as a fallback if method-level security is
    // enabled.
    @PostMapping("/users/create-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody CreateAdminRequest createAdminRequest) {
        try {
            User admin = userService.createAdminUser(createAdminRequest);
            return ResponseEntity.ok("Admin user created successfully: " + admin.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
