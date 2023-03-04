package io.c0dr.filemanager.service.exception;

public class WriteException extends BusinessException {

    private static final ErrorCode WRITE_ERROR_CODE = new ErrorCode(ErrorCode.ErrorType.UNKNOWN_ERROR, Severity.critical);

    public WriteException(String message, Throwable throwable) {
        super(WRITE_ERROR_CODE, message, throwable);
    }
}
