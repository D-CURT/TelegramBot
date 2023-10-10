package com.task.telegram.config;

import com.task.telegram.dto.UserDto;
import com.task.telegram.model.user.User;
import com.task.telegram.service.mapping.Mapper;
import com.task.telegram.service.mapping.TelegramBotModelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingConfig {

    @Bean
    public Mapper mapper() {
        TelegramBotModelMapper mapper = new TelegramBotModelMapper();
        configure(mapper);
        return mapper;
    }

    public void configure(ModelMapper mapper) {
        // TODO implement flexible mapping configuration mechanism
        TypeMap<User, UserDto> map = mapper.createTypeMap(User.class, UserDto.class);
        map.addMappings(mapping -> mapping.skip(UserDto::setPassword));
    }
}
