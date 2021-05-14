package com.bok.parent.integration.message;

import java.io.Serializable;

public class EmailMessage implements Serializable {
    public String to;
    public String subject;
    public String text;
}
