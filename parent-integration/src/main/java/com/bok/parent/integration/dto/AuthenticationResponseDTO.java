package com.bok.parent.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponseDTO {
    public String token;
    public String role;
}
