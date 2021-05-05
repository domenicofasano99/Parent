package com.bok.parent.audit;

import com.bok.integration.parent.dto.AccountLoginDTO;
import com.bok.integration.parent.dto.AccountRegistrationDTO;
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

    @Autowired
    AuditHelper auditHelper;

    @Before("@annotation(LoginAudit)")
    public void loginDetails(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountLoginDTO login = (AccountLoginDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountLoginDTO).findFirst().get();
        auditHelper.auditLoginRequest(req.getRemoteAddr(), login.email);
    }

    @Before("@annotation(RegisterAudit)")
    public void registerDetails(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountRegistrationDTO registration = (AccountRegistrationDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountRegistrationDTO).findFirst().get();
        auditHelper.auditRegistrationRequest(req.getRemoteAddr(), registration.credentials.email);
    }

}