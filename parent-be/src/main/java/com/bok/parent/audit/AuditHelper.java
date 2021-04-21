package com.bok.parent.audit;

import com.bok.parent.model.AuditLog;
import com.bok.parent.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuditHelper {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Async
    public void auditGatewayRequest(HttpServletRequest request, Long accountId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setAccountId(accountId);
        auditLog.setMethodName(request.getRequestURL().toString());
        auditLogRepository.save(auditLog);
    }

    @Async
    public void auditLoginRequest(String remoteAddr, String email) {
        AuditLog audit = new AuditLog();
        audit.setIpAddress(remoteAddr);
        audit.setEmail(email);
        audit.setMethodName("login");
        auditLogRepository.save(audit);
    }

    @Async
    public void auditRegistrationRequest(String remoteAddr, String email) {
        AuditLog audit = new AuditLog();
        audit.setIpAddress(remoteAddr);
        audit.setEmail(email);
        audit.setMethodName("register");
        auditLogRepository.save(audit);
    }
}
