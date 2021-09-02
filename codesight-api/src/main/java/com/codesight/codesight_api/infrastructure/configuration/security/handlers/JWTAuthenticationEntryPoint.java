package com.codesight.codesight_api.infrastructure.configuration.security.handlers;

import com.codesight.codesight_api.infrastructure.exception_handling.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;

@Component
public class JWTAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiError body = new ApiError(OffsetDateTime.now(), HttpStatus.UNAUTHORIZED.value(), "You must be logged in!", request.getRequestURI());

        if (request.getRequestURI().equals("/error")) {
            body = new ApiError(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Wrong email or password!", "/api/auth");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        OutputStream os = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(os, body);
        os.flush();
    }
}
