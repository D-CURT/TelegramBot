package com.task.telegram.service.crypto.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.telegram.dto.crypto.CryptoValue;
import com.task.telegram.dto.crypto.CryptoValues;
import com.task.telegram.exception.TelegramBotException;
import com.task.telegram.service.crypto.CryptoService;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class SimpleCryptoService implements CryptoService {

    public static final String SOURCE_URL = "https://api.mexc.com/api/v3/ticker/price";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<CryptoValue> getPrices() {
        URI uri = UriComponentsBuilder.fromUriString(SOURCE_URL).build().toUri();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), CryptoValues.class);
        } catch (Exception e) {
            throw new TelegramBotException("Unable to obtain prices");
        }
    }
}
