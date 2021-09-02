package com.codesight.codesight_api.infrastructure.configuration.security.handlers;

import com.codesight.codesight_api.infrastructure.exception_handling.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;

@Component
public class JWTAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError body = new ApiError(OffsetDateTime.now(), HttpStatus.FORBIDDEN.value(), "Wrong email or password!", request.getRequestURI());

        OutputStream os = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(os, body);
        os.flush();
    }
}
