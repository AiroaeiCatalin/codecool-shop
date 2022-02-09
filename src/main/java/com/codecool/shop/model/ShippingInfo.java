package com.codecool.shop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingInfo {
    private int userId;
    private String email;
    private String name;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private String zipcode;
    private String country;
    private String city;
}
