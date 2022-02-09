package com.codecool.shop.dao;

import com.codecool.shop.model.ShippingInfo;
import com.codecool.shop.model.User;

public interface UserDao {
    void add(User user);

    User find(String name);

    String findPassword(String email);

    void saveShippingInfo(int userId, String name, String email, String phoneNumber, String billingAddress, String shippingAddress, String country, String city, String zipcode);

    ShippingInfo findShippingInfo(int userId);
}
