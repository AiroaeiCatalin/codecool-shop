package com.codecool.shop.model;

import com.codecool.shop.utils.Utils;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class Order {

    @Expose
    private int id;
    private Cart cart;
    @Expose
    private float price;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String phoneNumber;
    @Expose
    private String billingAddress;
    @Expose
    private String shippingAddress;
    @Expose
    private String country;
    @Expose
    private String zipCode;
    @Expose
    private LocalDate localDate;
    @Expose
    private OrderStatus orderStatus;
    private CardDetails cardDetails;
    private int userId;

    public Order(Cart cart, float price, String name, String email, String phoneNumber, String billingAddress, String shippingAddress, String country, String zipCode) {
        this.cart = cart;
        this.price = price;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.country = country;
        this.zipCode = zipCode;
        localDate = LocalDate.now();
        id = Utils.randomTwoDigitId();
        orderStatus = OrderStatus.CHECKED_OUT;
    }

    public Order(int id, float price, String name, String email, String billingAddress, OrderStatus orderStatus, int userId, String country) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.email = email;
        this.billingAddress = billingAddress;
        this.country = country;
        this.orderStatus = orderStatus;
        this.userId = userId;

    }


}
