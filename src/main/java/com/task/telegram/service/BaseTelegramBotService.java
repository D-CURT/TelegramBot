package com.task.telegram.service;

import com.task.telegram.exception.ValidationException;
import com.task.telegram.model.BaseEntity;
import com.task.telegram.utils.GenericUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides basic functionality of {@link TelegramBotService}.
 *
 * @param <E> entity type.
 */
@Slf4j
@Getter
public abstract class BaseTelegramBotService<E extends BaseEntity> implements TelegramBotService<E> {
    private final Class<E> entityType = GenericUtils.findFirstGeneric(getClass());

    protected void setCodeIfValid(E entity, String errorMessage) {
        log.info("Resolving a new entity unique code...");
        Optional.ofNullable(entity.getCode())
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        value -> {
                            try {
                                entity.setCode(UUID.fromString(value).toString());
                            } catch (Exception e) {
                                throw ValidationException.getCodeMalformedException(errorMessage, value);
                            }
                        },
                        () -> entity.setCode(UUID.randomUUID().toString()));
    }
}
