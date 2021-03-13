package com.bok.parent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class SignUpRequest {

    public String username;
    public String email;
    public String password;
    public String name;
    public boolean mfa;


}
