package com.reportportal.exceptions;

public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String message, Object... args) {
        super(String.format(message, args));
    }

    public BaseException(String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
