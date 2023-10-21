package com.reportportal.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.vavr.control.Try;

@Component
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    public void info(String message, Object... args) {
        String messageToLog = format(message, args);
        LOGGER.info(messageToLog);
    }

    public void debug(String message, Object... args) {
        String messageToLog = "---> " + format(message, args);
        LOGGER.debug(messageToLog);
    }

    public void warn(String message, Object... args) {
        String messageToLog = "---> " + format(message, args);
        LOGGER.warn(messageToLog);
    }

    private String format(String message, Object... args){
        return Try.of(() -> String.format(message, args)).getOrElse(message);
    }

}
