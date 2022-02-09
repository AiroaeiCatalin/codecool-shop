package com.codecool.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CardDetails {
    private String cardOwner;
    private String cardNumber;
    private String expirationMonth;
    private String expirationYear;
}