package io.c0dr.filemanager.service.exception;

public class SecurityConstraintException extends BusinessException {

    public SecurityConstraintException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
