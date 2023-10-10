package com.task.telegram.service.crypto;

import com.task.telegram.dto.crypto.CryptoValue;

import java.util.List;

public interface CryptoService {

    List<CryptoValue> getPrices();
}
