package com.gbsfo.ecommerce.controller.handler;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.gbsfo.ecommerce.controller.ItemController;
import com.gbsfo.ecommerce.controller.OrderController;
import com.gbsfo.ecommerce.controller.PaymentController;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse.PublicApiError;
import com.gbsfo.ecommerce.controller.exception.PublicApiErrorResponse.PublicApiValidationError;
import com.gbsfo.ecommerce.controller.exception.ResourceAlreadyExistException;
import com.gbsfo.ecommerce.controller.exception.ResourceNotFoundException;
import com.gbsfo.ecommerce.controller.exception.ServiceValidationException;
import com.gbsfo.ecommerce.utils.ApiError;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import static com.gbsfo.ecommerce.utils.Constants.API_ERROR_INTERNAL_ERROR_MESSAGE;

@ControllerAdvice(assignableTypes = {OrderController.class, ItemController.class, PaymentController.class})
@Slf4j
public class ControllerExceptionHandler {

    @Autowired
    private Tracer tracer;

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<PublicApiErrorResponse> handleError(ConstraintViolationException ex) {
        log.error(ex.toString(), ex);
        List<PublicApiError> apiErrors = ex.getConstraintViolations().stream()
            .map(violation -> {
                //usually the format is - 'searchContacts.offset'
                var propertyPath = violation.getPropertyPath().toString();
                int dotIndex = propertyPath.lastIndexOf(".");
                if (dotIndex > 0 && dotIndex != propertyPath.length() - 1) {
                    var property = propertyPath.substring(dotIndex + 1);
                    return new PublicApiValidationError(violation.getMessage(), property);
                } else {
                    return new PublicApiValidationError(violation.getMessage(), propertyPath);
                }
            })
            .collect(Collectors.toList());
        var publicApiErrorResponse = new PublicApiErrorResponse(getRequestId(), apiErrors);
        return new ResponseEntity<>(publicApiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ApiError> handleError(ValidationException ex, WebRequest request) {
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

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<PublicApiErrorResponse> handleError(RuntimeException ex) {
        log.error(ex.toString(), ex);
        var apiError = new PublicApiError(API_ERROR_INTERNAL_ERROR_MESSAGE);
        var publicApiErrorResponse = new PublicApiErrorResponse(getRequestId(), List.of(apiError));
        return new ResponseEntity<>(publicApiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceValidationException.class)
    protected ResponseEntity<PublicApiErrorResponse> handleError(ServiceValidationException ex) {
        log.error(ex.toString(), ex);
        List<PublicApiError> apiErrors = ex.getViolations().stream()
            .map(violation -> new PublicApiValidationError(violation.getMessage(), violation.getProperty()))
            .collect(Collectors.toList());
        var publicApiErrorResponse = new PublicApiErrorResponse(getRequestId(), apiErrors);
        return new ResponseEntity<>(publicApiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ApiError> handleError(IllegalStateException ex, WebRequest request) {
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

    private String getRequestId() {
        Span currentSpan = tracer.currentSpan();
        return currentSpan == null ? null : currentSpan.context().traceId();
    }
}
