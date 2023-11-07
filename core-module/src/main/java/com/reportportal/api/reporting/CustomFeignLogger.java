package com.reportportal.api.reporting;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.reportportal.reporting.ReportService;
import com.reportportal.utils.ObjectFormatUtils;

@Component
public class CustomFeignLogger extends Logger {

    private final org.slf4j.Logger logger;
    private final Set<String> secretHeaders = Set.of("Authorization");

    @Autowired
    private ReportService reportService;

    public CustomFeignLogger() {
        this(feign.Logger.class);
    }

    public CustomFeignLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public CustomFeignLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    CustomFeignLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        String stringBodyToLog = "";
        if (request.body() != null) {
            String bodyText = request.charset() != null ? new String(request.body(), request.charset()) : null;
            stringBodyToLog = bodyText != null ? bodyText : "Binary data";
        }

        String message = traceRequest(request, stringBodyToLog);
        if (logLevel != Level.NONE && logger.isDebugEnabled()) {
            reportService.debug(message);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String stringBodyToLog = "";
        byte[] bodyData = null;
        if (response.body() != null) {
            bodyData = Util.toByteArray(response.body().asInputStream());
            stringBodyToLog = Util.decodeOrDefault(bodyData, Util.UTF_8, "Binary data");
        }
        String message = traceResponse(response, elapsedTime, stringBodyToLog);
        if (logLevel != Level.NONE && logger.isDebugEnabled()) {
            reportService.debug(message);
        }
        if (bodyData != null) {
            return response.toBuilder().body(bodyData).build();
        }
        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        String message = String.format(Logger.methodTag(configKey) + format, args);
        if (logger.isDebugEnabled()) {
            reportService.debug(message);
        }
    }

    private String traceRequest(Request request, String stringBodyToLog) {
        return String.format("%n-----------------------request begin-------------------------%n") +
                String.format("URL          : %s%n", request.url()) +
                String.format("Method       : %s%n", request.httpMethod()) +
                String.format("Headers      : %n%s", printHeaders(request.headers())) +
                String.format("Request body : %s%n", ObjectFormatUtils.toPrettyJson(stringBodyToLog)) +
                "-------------------------request end-------------------------";
    }

    private String traceResponse(Response response, long elapsedTime, String stringBodyToLog) {
        return String.format("%n------------------------response begin-----------------------%n") +
                String.format("Status code   : %s%n", response.status()) +
                String.format("Headers       : %n%s", printHeaders(response.headers())) +
                String.format("Response body : %s%n", ObjectFormatUtils.toPrettyJson(stringBodyToLog)) +
                String.format("Request time elapsed - %s ms.%n", elapsedTime) +
                "-------------------------response end------------------------";
    }

    private String printHeaders(Map<String, Collection<String>> headers) {
        StringBuilder builder = new StringBuilder();
        for (String field : headers.keySet()) {
            for (String value : Util.valuesOrEmpty(headers, field)) {
                if(secretHeaders.contains(field)){
                    value = ObjectFormatUtils.replaceWithStar(value);
                }
                builder.append("   ")
                        .append(field)
                        .append(" : ")
                        .append(value)
                        .append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

}
