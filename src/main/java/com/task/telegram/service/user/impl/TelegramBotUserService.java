package com.task.telegram.service.user.impl;

import com.task.telegram.dao.UserRepository;
import com.task.telegram.exception.user.UserExistsException;
import com.task.telegram.exception.user.UserNotFoundException;
import com.task.telegram.model.user.Providers;
import com.task.telegram.model.user.Roles;
import com.task.telegram.model.user.User;
import com.task.telegram.service.BaseTelegramBotService;
import com.task.telegram.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides functionality to operate with a {@link User}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotUserService extends BaseTelegramBotService<User> implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User getMe(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void create(User user) {
        log.info("Checking if a user with given username already exists...");
        if (repository.existsByUsername(user.getUsername()))
            throw new UserExistsException("Unable to create a user, such username already exists", user.getUsername());
        setCodeIfValid(user, "Unable to create a new user: provided code is malformed, check its format - UUID is required.");
        log.info("Applying USER role...");
        Optional.ofNullable(user.getRole()).ifPresentOrElse(user::setRole, () -> user.setRole(Roles.USER));
        resolveDisplayName(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Applying LOCAL authentication provider...");
        user.setProvider(Providers.LOCAL);
        repository.save(user);
    }

    // TODO implement update display name

    private void resolveDisplayName(User user) {
        log.info("Resolving a new user display name...");
        Optional.ofNullable(user.getDisplayName())
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        user::setDisplayName,
                        () -> user.setDisplayName(RandomStringUtils.random(10, true, true)));
    }
}
