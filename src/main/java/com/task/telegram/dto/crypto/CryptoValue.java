package com.task.telegram.dto.crypto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoValue {
    private String symbol;
    private Double price;
}
