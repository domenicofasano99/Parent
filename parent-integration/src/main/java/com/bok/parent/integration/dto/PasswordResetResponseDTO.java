package com.bok.parent.integration.dto;

import lombok.Data;

@Data
public class PasswordResetResponseDTO {
    public String message;

    public PasswordResetResponseDTO(String message) {
        this.message = message;
    }
}
