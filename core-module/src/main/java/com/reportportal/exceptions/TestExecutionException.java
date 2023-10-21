package com.reportportal.exceptions;

public class TestExecutionException extends BaseException {

    public TestExecutionException(String message, Object... args) {
        super(message, args);
    }

    public TestExecutionException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
