package com.task.telegram.model.user;

import com.task.telegram.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Contains a user details.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
@Accessors(chain = true)
@SequenceGenerator(name = "id_gen", sequenceName = "user_seq", allocationSize = 5)
public class User extends BaseEntity {

    @NotNull
    private String username;

    private String password;

    // TODO implement displayName inequality check
    private String displayName;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private Providers provider;

    private boolean enabled = true;
}
