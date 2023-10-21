package com.reportportal.exceptions;

public class AutomationException extends BaseException {

    public AutomationException(String message, Object... args) {
        super(message, args);
    }

    public AutomationException(Throwable cause) {
        super(cause);
    }
}
