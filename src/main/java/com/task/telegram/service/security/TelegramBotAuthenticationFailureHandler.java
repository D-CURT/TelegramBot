package com.task.telegram.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.telegram.model.http.CommonErrors;
import com.task.telegram.model.http.Response;
import com.task.telegram.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides authentication failures functionality.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;
    private final ResponseService responseService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Response error = responseService.buildError(null, CommonErrors.AUTHENTICATION.name(), CommonErrors.BAD_CREDENTIALS);
        log.error("Authentication failed:", exception);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        mapper.writeValue(response.getOutputStream(), error);
    }
}
