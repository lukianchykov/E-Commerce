//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gbsfo.ecommerce.controller.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String resourceIdentifier;

    public ResourceNotFoundException(String message, String resourceIdentifier) {
        super(message);
        this.resourceIdentifier = resourceIdentifier;
    }

    public ResourceNotFoundException(String message, Long resourceIdentifier) {
        super(message);
        this.resourceIdentifier = String.valueOf(resourceIdentifier);
    }

    public ResourceNotFoundException(String message, String resourceIdentifier, Exception e) {
        super(message, e);
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public String toString() {
        return this.getMessage() + "; Resource identifier = " + this.resourceIdentifier;
    }
}