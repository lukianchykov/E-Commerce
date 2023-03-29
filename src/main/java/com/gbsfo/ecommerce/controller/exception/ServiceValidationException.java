package com.gbsfo.ecommerce.controller.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ServiceValidationException extends RuntimeException {

    @Getter
    private List<Violation> violations = new ArrayList<>();

    public ServiceValidationException(String message) {
        super(message);
    }

    public ServiceValidationException(String message, Exception e) {
        super(message, e);
    }

    public ServiceValidationException(String message, List<Violation> violations) {
        super(message);
        this.violations = violations;
    }

    @Getter
    public static class Violation {

        private String message;

        private String property;

        public Violation(String message) {
            this.message = message;
        }

        public Violation(String message, String property) {
            this.message = message;
            this.property = property;
        }
    }

}
