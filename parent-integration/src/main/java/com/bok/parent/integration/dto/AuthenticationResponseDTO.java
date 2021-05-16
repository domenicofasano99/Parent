package com.bok.parent.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthenticationResponseDTO {
    public String token;
    public String role;
}
