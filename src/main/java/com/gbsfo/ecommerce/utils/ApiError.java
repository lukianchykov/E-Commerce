//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gbsfo.ecommerce.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ApiError {

    @JsonIgnore
    private Exception exception;

    private Map<String, Object> body = new HashMap();

    public ApiError(Exception exception) {
        this.exception = exception;
    }

    public Map<String, Object> getBody() {
        return this.body;
    }

    @JsonProperty("exception_class")
    public String getExceptionClass() {
        return this.exception.getClass().getName();
    }

    public String getMessage() {
        return this.exception.getMessage();
    }
}
