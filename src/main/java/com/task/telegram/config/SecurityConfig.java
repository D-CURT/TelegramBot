package com.task.telegram.config;

import com.task.telegram.dao.UserRepository;
import com.task.telegram.service.security.TelegramBotAuthenticationEntryPoint;
import com.task.telegram.service.security.TelegramBotAuthenticationFailureHandler;
import com.task.telegram.service.security.TelegramBotUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TelegramBotAuthenticationFailureHandler failureHandler;
    private final TelegramBotAuthenticationEntryPoint entryPoint;

    @Bean
    public UserDetailsService userDetailsService() {
        return new TelegramBotUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationEventPublisher(new DefaultAuthenticationEventPublisher(eventPublisher));
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider());
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .antMatchers("/", "/user/authorization").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationManager(authenticationManager(managerBuilder))
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/user")
                .failureHandler(failureHandler).and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/user")
                .userInfoEndpoint().oidcUserService(oidcUserService());
        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService service = new OidcUserService();
        return (userRequest) -> {
            OidcUser user = service.loadUser(userRequest);
            return new DefaultOidcUser(user.getAuthorities(), user.getIdToken(), user.getUserInfo());
        };
    }
}
