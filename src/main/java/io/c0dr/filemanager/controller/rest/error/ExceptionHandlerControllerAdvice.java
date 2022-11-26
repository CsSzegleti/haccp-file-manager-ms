package io.c0dr.filemanager.controller.rest.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.c0dr.filemanager.controller.rest.FileController;
import io.c0dr.filemanager.service.exception.BusinessException;
import io.c0dr.filemanager.service.exception.MissingEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice(assignableTypes = {FileController.class})
public class ExceptionHandlerControllerAdvice {

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleOthers(Exception ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @ExceptionHandler({Throwable.class})
    public ResponseEntity handleGenericException(Throwable ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    // BindException:
    //  pl.: Rest interface-en custom handlerArgumentResolver es converter nelkuli resolvalaskor dobott Bean validation
    // MethodArgumentTypeMismatchException:
    // pl.: Rest interface-en torteno org.springframework.core.convert.converter.Converter#convert exception eseten
    // MethodArgumentNotValidException:
    // pl.: Rest interface-en torteno Bean Validation hiba eseten
    @ExceptionHandler({BindException.class, MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    public ResponseEntity handleValidationException(BindException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MissingEntityException.class)
    public ResponseEntity handleMissingEntityException(MissingEntityException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({MissingRequestHeaderException.class})
    public ResponseEntity handleMissingRequestHeaderException(
            MissingRequestHeaderException ex) {
        log.error(ex.getLocalizedMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
