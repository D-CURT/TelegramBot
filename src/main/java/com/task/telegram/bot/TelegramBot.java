package com.task.telegram.bot;

import com.task.telegram.config.BotConfig;
import com.task.telegram.dao.TelegramUserRepository;
import com.task.telegram.dto.crypto.CryptoValue;
import com.task.telegram.exception.TelegramBotException;
import com.task.telegram.model.telegram.TelegramUser;
import com.task.telegram.service.crypto.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final CryptoService cryptoService;
    private final TelegramUserRepository repository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start": {
                    String message;
                    if (repository.isNotAvailable()) {
                        message = "Hi, " + update.getMessage().getChat().getFirstName() +
                                ". Unfortunately the bot currently is unavailable!";
                        sendMessage(chatId, message);
                        return;
                    }
                    String userName = update.getMessage().getChat().getUserName();
                    TelegramUser telegramUser = new TelegramUser().setTelegramUsername(userName);
                    telegramUser.setCode(UUID.randomUUID().toString());
                    //TODO update with chatId, to send a message to a user, once the prices updated.
                    repository.save(telegramUser);
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "We'll inform you, once prices updated";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(e.getMessage());
        }
    }
}
