package com.bok.parent.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    public String token;
    public LastAccessInfoDTO lastAccessInfo;
    public boolean passwordResetNeeded;
}
