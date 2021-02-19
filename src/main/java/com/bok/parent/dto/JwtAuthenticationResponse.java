package com.bok.parent.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAuthenticationResponse {

    public String accessToken;
    public boolean mfa;
}

