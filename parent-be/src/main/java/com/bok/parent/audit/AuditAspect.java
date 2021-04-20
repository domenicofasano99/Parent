package com.bok.parent.audit;

import com.bok.parent.dto.AccountLoginDTO;
import com.bok.parent.dto.AccountRegistrationDTO;
import com.bok.parent.model.AuditLog;
import com.bok.parent.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Before("@annotation(LoginAudit)")
    public void loginDetails(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountLoginDTO login = (AccountLoginDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountLoginDTO).findFirst().get();

        AuditLog audit = new AuditLog();
        audit.setIpAddress(req.getRemoteAddr());
        audit.setEmail(login.email);
        audit.setMethodName("login");
        auditLogRepository.save(audit);
    }

    @Before("@annotation(RegisterAudit)")
    public void registerDetails(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountRegistrationDTO registration = (AccountRegistrationDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountRegistrationDTO).findFirst().get();

        AuditLog audit = new AuditLog();
        audit.setIpAddress(req.getRemoteAddr());
        audit.setEmail(registration.email);
        audit.setMethodName("register");
        auditLogRepository.save(audit);
    }

}