package com.task.telegram.exception.user;

import org.springframework.http.HttpStatus;

/**
 * Thrown by the Quiz API when a user wasn't found by its username.
 */
public class UserNotFoundException extends UserException {

    /**
     * This constant provides an error code for the case when a user not found.
     * <p>The property key is - '<strong>api.errorCode.21</strong>'.
     * <p>See also: <strong>src/main/resources/messages.properties</strong>.
     */
    public static final int USER_NOT_FOUND_CODE = 21;
    public static final String DEFAULT_ERROR = "User not found";

    public UserNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, DEFAULT_ERROR, USER_NOT_FOUND_CODE, username);
    }
}
