package io.c0dr.filemanager.service.exception;

public class BusinessException extends Exception implements IError {

    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected BusinessException(ErrorCode errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode.ErrorType getErrorType() {
        return errorCode.getErrorType();
    }

    @Override
    public Severity getSeverity() {
        return errorCode.getSeverity();
    }
}
