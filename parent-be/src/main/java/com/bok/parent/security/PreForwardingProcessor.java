package com.bok.parent.security;

import com.bok.parent.audit.AuditHelper;
import com.bok.parent.service.SecurityService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class PreForwardingProcessor extends ZuulFilter {

    @Autowired
    SecurityService securityService;

    @Autowired
    AuditHelper auditHelper;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // Add a custom header in the request
        String token = request.getHeader("Authorization").replaceFirst("Bearer ", "");
        Long accountId = securityService.extractAccountId(token);
        Map<String, List<String>> queryParam = new HashMap<>();
        queryParam.put("accountId", Collections.singletonList(accountId.toString()));
        ctx.setRequestQueryParams(queryParam);
        auditHelper.auditRequest(request, accountId);
        return null;
    }

}