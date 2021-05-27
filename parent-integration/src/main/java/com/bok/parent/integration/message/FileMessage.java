package com.bok.parent.integration.message;

import java.io.Serializable;

public class FileMessage implements Serializable {
    public Long accountId;
    public byte[] fileByteArray;
    public String fileName;
    //no ., just extension (pdf, txt, docx, ect)
    public String fileExtension;
}
