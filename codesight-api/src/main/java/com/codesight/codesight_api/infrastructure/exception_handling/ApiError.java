package com.codesight.codesight_api.infrastructure.exception_handling;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApiError {

    private OffsetDateTime timestamp;
    private Integer status;
    private List<String> error;
    private String path;

    public ApiError(OffsetDateTime timestamp, Integer status, List<String> error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public ApiError(OffsetDateTime timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = List.of(error);
        this.path = path;
    }

    public String getTimestamp() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(timestamp);
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
