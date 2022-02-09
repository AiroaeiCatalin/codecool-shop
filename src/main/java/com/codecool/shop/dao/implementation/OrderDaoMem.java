package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {

    private final List<Order> data = new ArrayList<>();
    private static OrderDaoMem instance = null;

    @Override
    public void add(Order order, int userId){
        order.setId(data.size() + 1);
        data.add(order);
    }

    @Override
    public void updateOrder(String name, OrderStatus newStatus) {
        for (Order order: data){
            if (order.getName().equals(name)) {
                order.setOrderStatus(newStatus);
            }
        }
    }

    @Override
    public List<Order> getAllByUser(int userId) {
        return null;
    }

}
