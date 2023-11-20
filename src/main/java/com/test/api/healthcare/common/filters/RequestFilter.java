package com.test.api.healthcare.common.filters;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

@Slf4j
@Component
@Order(HIGHEST_PRECEDENCE)
public class RequestFilter implements Filter {

    private static final String REDACTED = "<Redacted>";
    private static final String KEY_REQUEST_ID = "requestId";
    private static final String RESPONSE_HEADER_REQUEST_ID = "EKZERO-RequestId";

    private static final String DOC_URL = "/docs/index.html";
    private static final String PROMETHEUS_URL = "/internal/prometheus";
    private static final String HEALTH_CHECK_URL = "/internal/health";
    private static final String REFRESH_TOKEN_URL = "/token/refresh";
    private static final String CLINIC_USER_URL = "/users";

    private final ObjectMapper objectMapper;

    private final String[] redactRequestBodyURLs = {REFRESH_TOKEN_URL};
    private final String[] redactResponseBodyURLs = {DOC_URL, PROMETHEUS_URL, REFRESH_TOKEN_URL, CLINIC_USER_URL};
    // Disable logging of health check API calls as they are creating a lot of noise
    private final String[] disableRequestLoggingURLs = {HEALTH_CHECK_URL, PROMETHEUS_URL};

    @Autowired
    public RequestFilter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {

        final HttpServletRequest servletRequest = (HttpServletRequest) request;
        final HttpServletResponse servletResponse = (HttpServletResponse) response;

        final ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(servletRequest);
        final ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(servletResponse);

        final Instant startTime = Instant.now();

        try {
            MDC.put(KEY_REQUEST_ID, UUID.randomUUID().toString());
            if (Arrays.stream(disableRequestLoggingURLs).noneMatch(contentCachingRequestWrapper.getRequestURI()::contains)) {
                log.info("START request: Method={} API={}", contentCachingRequestWrapper.getMethod(),
                    contentCachingRequestWrapper.getRequestURI());
                contentCachingResponseWrapper.addHeader(RESPONSE_HEADER_REQUEST_ID, MDC.get(KEY_REQUEST_ID));
            }
            chain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
        } finally {
            if (Arrays.stream(disableRequestLoggingURLs).noneMatch(contentCachingRequestWrapper.getRequestURI()::contains)
                || contentCachingResponseWrapper.getStatus() != HttpServletResponse.SC_OK
                && log.isInfoEnabled()) {
                log.info("Request Body: {}, Request Parameters: {}", getRequestBody(contentCachingRequestWrapper),
                    getRequestParameters(contentCachingRequestWrapper));
                log.info("Response body: {}", getResponseBody(contentCachingRequestWrapper, contentCachingResponseWrapper));
            }
            final long totalTime = Duration.between(startTime, Instant.now()).toMillis();
            log.info("END request: ResponseStatus={} Duration={}ms", contentCachingResponseWrapper.getStatus(), totalTime);
            // clean up MDC
            MDC.remove(KEY_REQUEST_ID);
            contentCachingResponseWrapper.copyBodyToResponse();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private String getRequestBody(final ContentCachingRequestWrapper requestWrapper) {
        if (Arrays.stream(redactRequestBodyURLs).anyMatch(requestWrapper.getRequestURI()::contains)) {
            return REDACTED;
        }
        final byte[] requestContentAsByteArray = requestWrapper.getContentAsByteArray();
        return requestContentAsByteArray.length != 0 ? new String(requestContentAsByteArray, StandardCharsets.UTF_8) : null;
    }

    private String getRequestParameters(final ContentCachingRequestWrapper requestWrapper) {
        final Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
        try {
            if (!parameterMap.isEmpty()) {
                return objectMapper.writeValueAsString(parameterMap);
            }
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponseBody(final ContentCachingRequestWrapper requestWrapper, final ContentCachingResponseWrapper responseWrapper) {
        if (Arrays.stream(redactResponseBodyURLs).anyMatch(requestWrapper.getRequestURI()::contains)) {
            return REDACTED;
        }
        final byte[] responseContentAsByteArray = responseWrapper.getContentAsByteArray();
        return responseContentAsByteArray.length != 0 ? new String(responseContentAsByteArray, StandardCharsets.UTF_8) : null;
    }
}
