package io.c0dr.filemanager.service.exception;

public interface IError {

    ErrorCode.ErrorType getErrorType();

    Severity getSeverity();
}
