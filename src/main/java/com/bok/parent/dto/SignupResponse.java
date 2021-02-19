package com.bok.parent.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignupResponse {
    public boolean mfa;
    public String secretImageUri;
}
