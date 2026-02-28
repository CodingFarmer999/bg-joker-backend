package com.bg.joker.controller;

import com.bg.joker.dto.FcmTestRequest;
import com.bg.joker.service.FcmService;
import com.bg.joker.service.UserDeviceTokenService;
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

    @Autowired
    private UserDeviceTokenService userDeviceTokenService;

    @PostMapping("/send")
    public ResponseEntity<String> sendTestNotification(@RequestBody FcmTestRequest request) {
        if (request.getToken() == null || request.getToken().isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required");
        }

        String response = fcmService.sendNotification(request.getToken(), request.getTitle(), request.getBody());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcastNotification(@RequestBody FcmTestRequest request) {
        String response = userDeviceTokenService.broadcastToAll(request.getTitle(), request.getBody(), fcmService);
        return ResponseEntity.ok(response);
    }
}
