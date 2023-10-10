package com.task.telegram.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${telegram.bot.name}")
    String botName;
    @Value("${telegram.bot.token}")
    String token;
}
