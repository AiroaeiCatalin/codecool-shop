package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.OrderStatus;

import java.util.List;

public interface OrderDao {
    void add(Order order, int userId);

    void updateOrder(String name, OrderStatus newStatus);

    List<Order> getAllByUser(int userId);
}
