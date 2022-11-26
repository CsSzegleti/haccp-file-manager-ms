package io.c0dr.filemanager.service.exception;

import lombok.Getter;


public class ErrorCode {

    public ErrorCode(ErrorType errorType, Severity severity) {
        this.errorType = errorType;
        this.severity = severity;
    }

    public enum ErrorType {
        MISSING_ENTITY,
        INVALID_ENTITY,
        ALREADY_EXISTS,
        INVALID_OPERATION,
        UNAUTHORIZED,
        UNKNOWN_ERROR;
    }

    @Getter
    private ErrorType errorType;
    @Getter
    private Severity severity;

}
