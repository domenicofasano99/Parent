package com.bok.parent.audit;

import com.bok.parent.model.AuditLog;
import com.bok.parent.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuditHelper {

    @Autowired
    AuditLogRepository auditLogRepository;

    //@Async
    public void auditRequest(HttpServletRequest request, Long accountId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setAccountId(accountId);
        auditLog.setMethodName(request.getRequestURL().toString());
        auditLogRepository.save(auditLog);
    }
}
