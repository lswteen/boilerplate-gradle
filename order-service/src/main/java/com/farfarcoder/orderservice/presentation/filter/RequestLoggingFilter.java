package com.farfarcoder.orderservice.presentation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Value("${spring.application.name:order-service}")
    private String serviceName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        String fullRequestId = serviceName + "-" + requestId;
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8)
                    .replaceAll("\\s+", " ").trim();
            String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8)
                    .replaceAll("\\s+", " ").trim();

            log.info("[RequestId: {}] [Duration: {}ms] [Method: {}] [URI: {}] [ReqBody: {}] [Status: {}] [ResBody: {}]",
                    fullRequestId,
                    duration,
                    request.getMethod(),
                    request.getRequestURI(),
                    requestBody.isEmpty() ? "N/A" : requestBody,
                    response.getStatus(),
                    responseBody.isEmpty() ? "N/A" : responseBody);

            responseWrapper.copyBodyToResponse();
        }
    }
}
