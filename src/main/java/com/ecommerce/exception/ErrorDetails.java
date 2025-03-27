package com.ecommerce.exception;

import java.time.LocalDateTime;

public class ErrorDetails {
    private boolean success;
    private String errorMessage;
    private String uri;
    private LocalDateTime timestamp;

    public ErrorDetails(boolean success, String errorMessage, String uri, LocalDateTime timestamp) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.uri = uri;
        this.timestamp = timestamp;
    }

    public ErrorDetails() {
        super();
    }

    public ErrorDetails(boolean success, LocalDateTime timestamp, String uri, String errorMessage) {
        this.success = success;
        this.timestamp = timestamp;
        this.uri = uri;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUri() {
        return uri;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
