package com.bok.parent.integration.dto;

import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    public String token;
    public String role;
}
