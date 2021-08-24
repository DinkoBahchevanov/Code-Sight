package com.codesight.codesight_api.infrastructure.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ExceptionUtil {

    private ExceptionUtil() {

    }

    public static Map<String, Object> getBody(Exception ex, HttpStatus status) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", ex.getMessage());
        body.put("path", "api/v1/challenges");
        return body;
    }
}
