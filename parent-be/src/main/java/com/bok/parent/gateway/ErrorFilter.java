package com.bok.parent.gateway;

import com.bok.parent.be.exception.ApiError;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorFilter extends ZuulFilter {

    private static final String FILTER_TYPE = "error";
    private static final String THROWABLE_KEY = "throwable";
    private static final int FILTER_ORDER = -1;

    @Override
    public String filterType() {
        return FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        final RequestContext context = RequestContext.getCurrentContext();
        final Object throwable = context.get(THROWABLE_KEY);

        if (throwable instanceof ZuulException) {
            final ZuulException zuulException = (ZuulException) throwable;
            log.error("Zuul failure detected, cause {} stacktrace {}", zuulException.getCause(), zuulException.getStackTrace());

            // remove error code to prevent further error handling in follow up filters
            context.remove(THROWABLE_KEY);

            // populate context with new response values
            ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, zuulException.errorCause, null);
            context.setResponseBody(error.toString());
            context.getResponse().setContentType("application/json");
            // can set any error code as excepted
            context.setResponseStatusCode(error.getStatus().value());
        }
        return null;
    }
}
