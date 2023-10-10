package com.task.telegram.controller;

import com.task.telegram.dto.UserDto;
import com.task.telegram.model.http.Response;
import com.task.telegram.model.user.TelegramBotUserDetails;
import com.task.telegram.model.user.User;
import com.task.telegram.service.mapping.Mapper;
import com.task.telegram.service.response.ResponseService;
import com.task.telegram.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Provides all endpoints linked to the <strong>User</strong>.
 * <p>Includes <strong>authorization</strong> and <strong>logout</strong>.
 * <p>Version: <strong>V0</strong>.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserControllerV0 {

    private final ResponseService responseService;
    private final UserService service;
    private final Mapper mapper;

    @GetMapping(value = StringUtils.EMPTY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMe() {
        User me = service.getMe(getCurrentUsername());
        return responseService.build(mapper.map(me, UserDto.class));
    }

    /**
     * Authorizes a user with accepted details.
     *
     * @param data details of the user to authorize
     * @return Response with authorized user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/authorization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response authorize(@RequestBody @Validated(UserDto.Authorization.class) UserDto data) {
        User user = mapper.map(data, User.class);
        service.create(user);
        mapper.map(user, data);
        return responseService.build(data);
    }

    /**
     * Fetches a username of the currently authenticated user.
     *
     * @return the current username.
     */
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof DefaultOidcUser oidcUser) {
            username = oidcUser.getEmail();
        } else if (principal instanceof TelegramBotUserDetails quizUser) {
            username = quizUser.getUsername();
        }
        return username;
    }
}
