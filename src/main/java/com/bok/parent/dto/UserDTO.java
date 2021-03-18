package com.bok.parent.dto;

import com.bok.parent.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    public String username;
    public String password;
    public User.Role role;
    public String email;
}
