package com.ey.desafiotecnico.exception;

import com.fasterxml.jackson.core.JacksonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        LOGGER.error(ex.getLocalizedMessage(), ex);
        CustomExceptionResponse customException = new CustomExceptionResponse();

        if (ex instanceof UserException) {
            customException.setMensaje(ex.getMessage());
            return handleExceptionInternal(ex, customException, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
        }

        return handleExceptionInternal(ex, customException, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
