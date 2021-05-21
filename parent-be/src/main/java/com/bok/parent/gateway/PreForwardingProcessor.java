package com.bok.parent.gateway;

import com.bok.parent.helper.AuditHelper;
import com.bok.parent.service.SecurityService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;


@Component
@Slf4j
public class PreForwardingProcessor extends ZuulFilter {

    @Autowired
    SecurityService securityService;

    @Autowired
    AuditHelper auditHelper;

    @Override
    public String filterType() {
        return PRE_TYPE;
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

        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        securityService.checkTokenValidity(token);
        Long accountId = securityService.getAccountId(token);

        auditHelper.auditGatewayRequest(request, accountId);

        // Add a custom header in the request
        Map<String, List<String>> queryParam = new HashMap<>();
        queryParam.put("accountId", Collections.singletonList(accountId.toString()));
        ctx.setRequestQueryParams(queryParam);
        return null;
    }

}