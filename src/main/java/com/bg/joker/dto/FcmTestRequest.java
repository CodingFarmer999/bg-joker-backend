package com.bg.joker.dto;

import lombok.Data;

@Data
public class FcmTestRequest {
    private String token;
    private String title;
    private String body;
}
