package com.bok.parent.gateway;

import com.bok.parent.be.helper.AuditHelper;
import com.bok.parent.be.service.SecurityService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;


@Component
@Slf4j
public class PreForwardFilter extends ZuulFilter {

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
        boolean valid = securityService.checkTokenValidity(token);
        if (valid) {
            Long accountId = securityService.getAccountId(token);
            auditHelper.auditGatewayRequest(request, accountId);
            //preserve passed query-params
            Map<String, List<String>> queryParams = ctx.getRequestQueryParams();
            if (isNull(queryParams)) {
                queryParams = new HashMap<>();
            }
            queryParams.put("accountId", Collections.singletonList(accountId.toString()));
            log.info("Query params: {}", queryParams.keySet());
            ctx.setRequestQueryParams(queryParams);
        } else {
            ctx.set("error.status_code", HttpServletResponse.SC_UNAUTHORIZED);
            ctx.set("error.exception", "Error in token verification.");
        }
        return null;
    }
}