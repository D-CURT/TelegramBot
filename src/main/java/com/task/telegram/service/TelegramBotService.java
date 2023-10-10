package com.task.telegram.service;

import com.task.telegram.model.BaseEntity;

/**
 * Provides all entity related functionality.
 *
 * @param <E> entity type.
 */
public interface TelegramBotService<E extends BaseEntity> {

    /**
     * Creates a new entity.
     *
     * @param entity entity to save in the system.
     */
    void create(E entity);

    /**
     * @return entity type.
     */
    Class<E> getEntityType();
}
