package com.quasar.operation.exceptions;

import com.google.gson.annotations.SerializedName;
import com.quasar.operation.utils.annotations.Exclude;
import org.springframework.http.HttpStatus;

public class RequestException extends RuntimeException {
    @Exclude
    private final int statusCode;
    @SerializedName("ERROR")
    private final String message;

    public RequestException(HttpStatus status) {
        this.message = status.getReasonPhrase();
        this.statusCode = status.value();
    }

    public RequestException(HttpStatus status, String message) {
        this.message = message;
        this.statusCode = status.value();
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
