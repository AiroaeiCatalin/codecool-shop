package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.util.List;

public interface CartDao {
    void addCart(int userId, Cart cart);

    int getSavedCartId(int userId);

    List<Integer> getCartsForUser(int userId);

    Cart getCartWithItems(int cartId);

    void addCartToSavedCarts(int userId, Cart cart);
}
