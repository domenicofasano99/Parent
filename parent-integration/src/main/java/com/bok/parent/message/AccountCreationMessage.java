package com.bok.parent.message;

import java.io.Serializable;

public class AccountCreationMessage implements Serializable {
    public Long accountId;
    public String name;
    public String surname;
    public String email;
}
