package com.task.telegram.dao;

import com.task.telegram.model.telegram.TelegramUser;
import org.springframework.data.repository.CrudRepository;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {
    public static final int MAX_USERS = 100;
    default boolean isAvailable() {
        return count() < MAX_USERS;
    }

    default boolean isNotAvailable() {
        return !isAvailable();
    }
}
