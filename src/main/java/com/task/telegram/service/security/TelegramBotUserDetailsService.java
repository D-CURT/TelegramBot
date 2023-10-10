package com.task.telegram.service.security;

import com.task.telegram.dao.UserRepository;
import com.task.telegram.model.user.TelegramBotUserDetails;
import com.task.telegram.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Provides functionality to operate with security user details.
 */
public record TelegramBotUserDetailsService(UserRepository userRepository) implements UserDetailsService {
    /**
     * Creates user details based on an existing user.
     *
     * @param username username field value.
     *
     * @return created {@link UserDetails}.
     *
     * @throws UsernameNotFoundException if a user wasn't found by an accepted username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new TelegramBotUserDetails(userRepository.findByUsername(username)
                .filter(User::isEnabled)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user by username '%s'"
                        .formatted(username))));
    }
}
