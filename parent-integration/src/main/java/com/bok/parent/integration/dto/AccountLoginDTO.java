package com.bok.parent.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginDTO {
    public String email;

    @ToString.Exclude
    public String password;
}
