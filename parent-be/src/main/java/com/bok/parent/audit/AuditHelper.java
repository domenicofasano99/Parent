package com.bok.parent.audit;

import com.bok.parent.model.AuditLog;
import com.bok.parent.repository.AuditLogRepository;
import com.google.common.io.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AuditHelper {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Async
    public void auditGatewayRequest(HttpServletRequest request, Long accountId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setAccountId(accountId);
        auditLog.setMethod(request.getMethod());
        if (request.getMethod().equalsIgnoreCase("post")) {
            String payload = null;
            try {
                payload = CharStreams.toString(request.getReader());
            } catch (IOException ioe) {
                payload = "Error while reading the request payload";
            } finally {
                auditLog.setPayload(payload);
            }
        } else {
            if (!request.getParameterMap().isEmpty()) {
                auditLog.setParameters(StringUtils.join(request.getParameterMap()));
            }
        }
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
