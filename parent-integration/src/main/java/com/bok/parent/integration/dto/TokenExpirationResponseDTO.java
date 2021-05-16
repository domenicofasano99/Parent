package com.bok.parent.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenExpirationResponseDTO {
    public Instant expirationDate;
    public Boolean expired;

    public TokenExpirationResponseDTO(Boolean expired) {
        this.expired = expired;
    }

}
