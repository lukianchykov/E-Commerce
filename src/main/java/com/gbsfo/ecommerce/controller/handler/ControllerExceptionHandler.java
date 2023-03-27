package com.gbsfo.ecommerce.controller.handler;

import javax.validation.ConstraintViolationException;

import com.gbsfo.ecommerce.controller.ItemController;
import com.gbsfo.ecommerce.controller.OrderController;
import com.gbsfo.ecommerce.controller.PaymentController;
import com.gbsfo.ecommerce.controller.exception.ResourceAlreadyExistException;
import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.utils.ApiError;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(assignableTypes = {OrderController.class, ItemController.class, PaymentController.class})
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleError(ConstraintViolationException ex, WebRequest request) {
        log.error(ex.toString(), ex);
        ApiError apiError = new ApiError(ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    protected ResponseEntity<ApiError> handleError(ResourceAlreadyExistException ex, WebRequest request) {
        log.error(ex.toString(), ex);
        ApiError apiError = new ApiError(ex);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ApiError> handleError(ResourceNotFoundException ex, WebRequest request) {
        log.error(ex.toString(), ex);
        ApiError apiError = new ApiError(ex);
        apiError.getBody().put("missing_resource", ex.getResourceIdentifier());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiError> handleError(IllegalArgumentException ex, WebRequest request) {
        log.error(ex.toString(), ex);
        ApiError apiError = new ApiError(ex);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
