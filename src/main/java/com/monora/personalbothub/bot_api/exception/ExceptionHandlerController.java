package com.monora.personalbothub.bot_api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessage> handleApiException(ApiException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                ex.getStatusCode(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, ex.getErrorType().getStatus());
    }

}
