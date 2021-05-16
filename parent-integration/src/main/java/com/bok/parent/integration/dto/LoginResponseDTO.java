package com.bok.parent.integration.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginResponseDTO {
    public String token;
    public String lastAccessIP;
    public LocalDateTime lastAccessDateTime;
}
