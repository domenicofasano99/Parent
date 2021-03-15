package com.bok.parent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    public String username;
    public String password;
    public String role;
    public String email;
}
