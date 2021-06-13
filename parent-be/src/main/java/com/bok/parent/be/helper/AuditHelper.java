package com.bok.parent.be.helper;

import com.bok.parent.model.AccessInfo;
import com.bok.parent.model.AuditLog;
import com.bok.parent.repository.AccessInfoRepository;
import com.bok.parent.repository.AuditLogRepository;
import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class AuditHelper {

    @Autowired
    AuditLogRepository auditLogRepository;

    @Autowired
    AccessInfoRepository accessInfoRepository;

    @Autowired
    AccountHelper accountHelper;

    @Async
    public void auditGatewayRequest(HttpServletRequest request, Long accountId) {
        AuditLog auditLog = new AuditLog();
        auditLog.setIpAddress(request.getRemoteAddr());
        auditLog.setAccountId(accountId);
        auditLog.setMethod(request.getMethod());
        if (request.getMethod().equalsIgnoreCase("get")) {
            try {
                auditLog.setParameters(getRequestParameters(request));
            } catch (NullPointerException ignored) {
            }
        }
        auditLog.setPayload(getRequestPayload(request));
        auditLog.setPath(getRequestPath(request));
        auditLogRepository.save(auditLog);
    }

    @Async
    public void auditGatewayResponse() {
    }

    @Async
    public void auditLoginRequest(String remoteAddr, String email) {
        AuditLog audit = new AuditLog();
        audit.setIpAddress(remoteAddr);
        audit.setEmail(email);
        audit.setPath("/login");
        audit.setMethod(HttpMethod.POST.name());
        auditLogRepository.save(audit);
    }

    @Async
    public void auditRegistrationRequest(String remoteAddr, String email) {
        AuditLog audit = new AuditLog();
        audit.setIpAddress(remoteAddr);
        audit.setEmail(email);
        audit.setPath("/register");
        audit.setMethod(HttpMethod.POST.name());
        auditLogRepository.save(audit);
    }

    public String getRequestPayload(HttpServletRequest request) {
        String payload;
        try {
            payload = CharStreams.toString(request.getReader());
        } catch (IOException ioe) {
            log.info("Error while retrieving request payload for {}", request);
            payload = null;
        }
        return payload;
    }

    public String getRequestParameters(HttpServletRequest request) {
        if (isNull(request) || MapUtils.isEmpty(request.getParameterMap()) || request.getParameterMap().isEmpty()) {
            return null;
        }
        return StringUtils.join(request.getParameterMap());
    }

    public String getRequestPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();

        StringBuilder url = new StringBuilder();
        url.append(contextPath).append(servletPath);

        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    public AccessInfo findLastAccessInfo(Long accountId) {
        Optional<AccessInfo> accessInfo = accessInfoRepository.findLastAccessInfoByAccountId(accountId);
        return accessInfo.orElse(null);
    }

    @Async
    public void saveAccessInfo(String remoteAddr, String email) {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setAccount(accountHelper.findByEmail(email));
        accessInfo.setIpAddress(remoteAddr);
        accessInfoRepository.save(accessInfo);
    }

    @Async
    public void auditPasswordResetRequest(String remoteAddr, String email) {
        AuditLog audit = new AuditLog();
        audit.setIpAddress(remoteAddr);
        audit.setEmail(email);
        audit.setPath("/resetPassword");
        auditLogRepository.save(audit);
    }
}
