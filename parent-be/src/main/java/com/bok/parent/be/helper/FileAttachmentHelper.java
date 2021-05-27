package com.bok.parent.be.helper;

import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.integration.message.FileMessage;
import com.bok.parent.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
@Component
public class FileAttachmentHelper {

    private final AccountHelper accountHelper;
    private final MessageHelper messageHelper;
    private final S3Helper s3Helper;

    @Autowired
    public FileAttachmentHelper(AccountHelper accountHelper, MessageHelper messageHelper, S3Helper s3Helper) {
        this.accountHelper = accountHelper;
        this.messageHelper = messageHelper;
        this.s3Helper = s3Helper;
    }

    public void handle(FileMessage message) {
        Account account = accountHelper.findById(message.accountId);
        Path path;
        try {
            path = createTemporaryFile(message.fileByteArray, message.fileName, message.fileExtension);
            s3Helper.upload(account.getId(), path);
        } catch (IOException ex) {
            log.error("Error while handling report message");
        }

        EmailMessage emailMessage = new EmailMessage();
    }


    public Path createTemporaryFile(byte[] document, String fileName, String extension) throws IOException {
        Path temp;
        try {
            temp = Files.createTempFile(fileName, "." + extension);
            Files.write(temp, document);
            log.info(temp.toString());
        } catch (IOException ioException) {
            log.error("Error occurred while writing file to temp directory, {}", ioException.getCause().toString());
            return null;

        }
        return temp;

    }


}
