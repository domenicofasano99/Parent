package com.bok.parent.helper;

import com.bok.parent.integration.message.EmailMessage;
import com.bok.parent.integration.message.ReportMessage;
import com.bok.parent.model.Account;
import com.bok.report.ReportGenerator;
import com.itextpdf.text.Document;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportHelper {

    final ReportGenerator reportGenerator;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    MessageHelper messageHelper;

    @Autowired
    S3Helper s3Helper;

    public ReportHelper() {
        this.reportGenerator = new ReportGenerator();
    }

    @SneakyThrows
    public void handle(ReportMessage message) {
        Account account = accountHelper.findById(message.accountId);
        Document report = reportGenerator.generateReport(account.getId());

        EmailMessage emailMessage = new EmailMessage();
    }
}
