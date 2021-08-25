package com.codesight.codesight_api.infrastructure.exceptions;

public class ApiError {

    private String timestamp;
    private String error;
    private String path;
    private String status;

    public ApiError(String timestamp, String error, String path, String status) {
        this.timestamp = timestamp;
        this.error = error;
        this.path = path;
        this.status = status;
    }

    public ApiError() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
