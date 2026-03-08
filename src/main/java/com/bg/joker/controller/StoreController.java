package com.bg.joker.controller;

import com.bg.joker.entity.Store;
import com.bg.joker.entity.User;
import com.bg.joker.mapper.StoreAdminMapper;
import com.bg.joker.repository.StoreRepository;
import com.bg.joker.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreRepository storeRepository;
    private final StoreAdminMapper storeAdminMapper;
    private final UserRepository userRepository;

    public StoreController(StoreRepository storeRepository, StoreAdminMapper storeAdminMapper,
            UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.storeAdminMapper = storeAdminMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Store>> getAllActiveStores() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Check if user has ROLE_ADMIN
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                // Fetch user from DB to get the ID
                User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
                if (user != null) {
                    // Fetch only stores this admin is assigned to via MyBatis logic
                    List<Store> adminStores = storeAdminMapper.findActiveStoresByAdminId(user.getId());
                    return ResponseEntity.ok(adminStores);
                }
            }
        }

        // If not logged in, or is a regular user (not admin), or user not found, return
        // all stores
        List<Store> stores = storeRepository.findAll();
        // Here we could add a filter if we only wanted active ones using stream or jpa
        // method
        // For MVP returning all standard stores
        return ResponseEntity.ok(stores);
    }
}
