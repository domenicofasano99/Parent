package com.bok.parent.utils.encryption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    public Instant issuedAt;
    public Instant expiresAt;
    public String issuer;
    public Long accountId;
    public String email;
    public Boolean expired;
}
