package com.codecool.shop.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LineItem {

    private Product product;

    private float price;

    private int quantity;

    public LineItem(Product product) {
        this.product = product;
        quantity = 1;
        price = product.getDefaultPrice();
    }

    public void add() {
        quantity++;
        price = quantity * price;
    }

    public void remove() {
        quantity--;
        price = quantity * price;
    }

    public void updatePrice() {
        price = quantity * product.getDefaultPrice();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updatePrice();
    }
}
