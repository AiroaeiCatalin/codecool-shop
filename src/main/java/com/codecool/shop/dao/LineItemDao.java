package com.codecool.shop.dao;

import com.codecool.shop.model.LineItem;

import java.util.List;

public interface LineItemDao {
    void add(LineItem lineItem, int userId, int cartId);

    List<LineItem> getLineItems(int cartId);
}

