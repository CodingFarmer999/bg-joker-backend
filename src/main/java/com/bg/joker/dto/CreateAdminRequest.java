package com.bg.joker.dto;

import lombok.Data;

@Data
public class CreateAdminRequest {
    private String username;
    private String email;
    private String password;
    private String displayName;
}
