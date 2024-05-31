package com.mobiauto.data;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class ApiResponseDTO {

    String message;
    Object result;

    public ApiResponseDTO() {
    }

    public ApiResponseDTO(String message, Object result) {
        this.message = message;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


}
