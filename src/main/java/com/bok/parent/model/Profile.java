package com.bok.parent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String displayName;
    private String profilePictureUrl;
    private Date birthday;
}

