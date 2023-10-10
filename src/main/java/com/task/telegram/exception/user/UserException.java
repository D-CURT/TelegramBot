package com.task.telegram.exception.user;

import com.task.telegram.exception.TelegramBotException;
import org.springframework.http.HttpStatus;

/**
 * The abstract user exception.
 * <p>Provides common logic for all of the {@link com.quiz.javaquizapi.model.user.User} related errors.
 */
public abstract class UserException extends TelegramBotException {

    public UserException(HttpStatus status, String message, int code, String username) {
        super(status, message, code, username);
    }

    @Override
    protected String getGroup() {
        return "user";
    }
}
