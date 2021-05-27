package com.bok.parent.integration.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage implements Serializable {
    public String to;
    public String subject;
    public String body;
    public List<Attachment> attachments;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attachment implements Serializable {
        public Long accountId;
        public byte[] fileByteArray;
        public String fileName;
        //no ., just extension (pdf, txt, docx, ect)
        public String fileExtension;
    }
}
