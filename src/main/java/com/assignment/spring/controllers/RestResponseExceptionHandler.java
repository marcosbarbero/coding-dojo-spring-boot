package com.assignment.spring.controllers;

import com.assignment.spring.models.ApiError;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class RestResponseExceptionHandler
    extends ResponseEntityExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<Object> handleClientError(HttpClientErrorException ex,
        WebRequest request) {
        ApiError apiError = ApiError.builder().status(ex.getStatusCode())
            .message(ex.getLocalizedMessage())
            .errors(List.of("Errors with the weather api")).build();
        return new ResponseEntity<Object>(
            apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
            .message(ex.getLocalizedMessage())
            .errors(List.of("Server Error")).build();
        return new ResponseEntity<Object>(
            apiError, new HttpHeaders(), apiError.getStatus());
    }
}