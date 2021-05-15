package com.bok.parent.audit;

import com.bok.parent.integration.dto.AccountLoginDTO;
import com.bok.parent.integration.dto.AccountRegistrationDTO;
import com.bok.parent.helper.AuditHelper;
import com.bok.parent.integration.dto.PasswordResetRequestDTO;
import com.bok.parent.integration.dto.PasswordResetResponseDTO;
import com.bok.parent.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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

    @After("@annotation(LoginAudit)")
    public void saveLoginAccessInfo(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountLoginDTO login = (AccountLoginDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountLoginDTO).findFirst().get();
        auditHelper.saveAccessInfo(req.getRemoteAddr(), login.email);
    }

    @Before("@annotation(RegisterAudit)")
    public void registerDetails(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        AccountRegistrationDTO registration = (AccountRegistrationDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof AccountRegistrationDTO).findFirst().get();
        auditHelper.auditRegistrationRequest(req.getRemoteAddr(), registration.credentials.email);
    }

    @Before("@annotation(PasswordResetAudit)")
    public void passwordResetDetails(JoinPoint joinPoint) {
        HttpServletRequest req = (HttpServletRequest) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof HttpServletRequest).findFirst().get();
        PasswordResetRequestDTO resetRequest = (PasswordResetRequestDTO) Arrays.stream(joinPoint.getArgs()).filter(a -> a instanceof PasswordResetRequestDTO).findFirst().get();
        auditHelper.auditPasswordResetRequest(req.getRemoteAddr(), resetRequest.email);
    }

}