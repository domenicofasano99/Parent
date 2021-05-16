package com.bok.parent.integration.dto;

import lombok.Data;

@Data
public class AccountRegistrationResponseDTO {
    public String status;

    public AccountRegistrationResponseDTO(String status) {
        this.status = status;
    }
}
