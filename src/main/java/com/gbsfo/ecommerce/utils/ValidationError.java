//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gbsfo.ecommerce.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {

    private String field;

    private String message;

    private String code;

    @JsonProperty("rejected_value")
    private Object rejectedValue;

    public ValidationError(String field, String message, String code, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
        this.code = code;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getRejectedValue() {
        return this.rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}