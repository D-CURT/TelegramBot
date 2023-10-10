package com.task.telegram.exception;

import com.task.telegram.model.http.CommonErrors;
import com.task.telegram.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Catches all the API exceptions and handles them properly.
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final ResponseService responseService;

    /**
     * Handles all unexpected {@link Exception} and its extensions.
     *
     * @param exception thrown exception.
     * @param request   the current request details.
     * @return {@link com.task.telegram.model.http.Response} wrapped by {@link ResponseEntity}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleBasicException(Exception exception, WebRequest request) {
        log.error("Unexpected error occurred:", exception);
        return handleExceptionInternal(
                exception,
                responseService.buildError(null, CommonErrors.UNEXPECTED.name(), CommonErrors.UNEXPECTED_ERROR_CODE),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    /**
     * Handles {@link TelegramBotException} and its extensions.
     *
     * @param exception thrown API exception.
     * @param request   the current request details.
     * @return {@link com.task.telegram.model.http.Response} wrapped by {@link ResponseEntity}.
     */
    @ExceptionHandler(TelegramBotException.class)
    public ResponseEntity<?> handleBasicException(TelegramBotException exception, WebRequest request) {
        log.error(exception.getMessage());
        return handleExceptionInternal(
                exception,
                responseService.buildError(exception.getData(), exception.getGroup(), exception.getCode(), exception.getArgs()),
                new HttpHeaders(),
                exception.getStatus(),
                request);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} thrown during DTOs validation.
     *
     * @param exception thrown validation exception.
     * @param headers   the current request headers.
     * @param status    default HTTP status.
     * @param request   the current request details.
     * @return {@link Response} wrapped by {@link ResponseEntity}.
     */
    @Override
    @SuppressWarnings("ALL")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        return handleExceptionInternal(
                exception,
                responseService.buildError(null, CommonErrors.VALIDATION.name(), defaultMessage, fieldError.getField()),
                new HttpHeaders(),
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }
}
