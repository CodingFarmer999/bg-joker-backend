package com.bg.joker.dto;

import lombok.Data;

@Data
public class FcmTokenRequest {
    private String token;
    private String deviceType = "WEB"; // Default to WEB since we are building a React app
}
