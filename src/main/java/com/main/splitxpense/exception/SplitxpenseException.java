package com.main.splitxpense.exception;

public class SplitxpenseException extends RuntimeException {
    private String errorCode;

    public SplitxpenseException(String message) {
        super(message);
    }

    public SplitxpenseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SplitxpenseException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
