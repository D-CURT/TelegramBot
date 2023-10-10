package com.task.telegram.model.telegram;

import com.task.telegram.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "telegram_users")
@SequenceGenerator(name = "id_gen", sequenceName = "telegram_user_seq", allocationSize = 5)
public class TelegramUser extends BaseEntity {
    private String telegramUsername;
}
