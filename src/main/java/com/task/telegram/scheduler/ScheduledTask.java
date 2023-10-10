package com.task.telegram.scheduler;

import com.task.telegram.dao.TelegramUserRepository;
import com.task.telegram.dto.crypto.CryptoValue;
import com.task.telegram.model.telegram.TelegramUser;
import com.task.telegram.service.crypto.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class ScheduledTask {

    public static final int INTERVAL = 20000;

    private final CryptoService cryptoService;
    private final TelegramUserRepository telegramUserRepository;

    @Scheduled(fixedRate = INTERVAL)
    public void updatePrices() {
        List<CryptoValue> prices = cryptoService.getPrices();
        Iterable<TelegramUser> users = telegramUserRepository.findAll();
    }
}
