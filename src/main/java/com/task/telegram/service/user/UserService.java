package com.task.telegram.service.user;

import com.task.telegram.model.user.User;
import com.task.telegram.service.TelegramBotService;

/**
 * Provides functionality to operate with a {@link User}.
 */
public interface UserService extends TelegramBotService<User> {
    User getMe(String username);
}
