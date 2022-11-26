package io.c0dr.filemanager.service.exception;

public class MissingEntityException extends BusinessException {

    private static final ErrorCode MISSING_ENTITY_ERROR_CODE = new ErrorCode(ErrorCode.ErrorType.MISSING_ENTITY, Severity.critical);


    public MissingEntityException(Object id) {
        super(MISSING_ENTITY_ERROR_CODE, "Cannot find entity with id: " + id);
    }

    public MissingEntityException(Object id, String message) { // TODO: 2022. 08. 02. BZoli: id is not used
        super(MISSING_ENTITY_ERROR_CODE, message);
    }

    public MissingEntityException(String message) {
        super(MISSING_ENTITY_ERROR_CODE, message);
    }

    public MissingEntityException(String message, Throwable cause) {
        super(MISSING_ENTITY_ERROR_CODE, message, cause);
    }

    public MissingEntityException(Throwable cause) {
        super(MISSING_ENTITY_ERROR_CODE, cause);
    }

    protected MissingEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MISSING_ENTITY_ERROR_CODE, message, cause, enableSuppression, writableStackTrace);
    }
}