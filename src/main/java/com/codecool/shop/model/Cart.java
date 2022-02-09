package com.codecool.shop.model;

import com.codecool.shop.utils.AdminLog;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Cart {
    private int id;

    private List<LineItem> items;

    private LocalDate date;

    private float price = 0;

    public Cart() {
        AdminLog.getInstance().addLog("Created cart.");
        items = new ArrayList<>();
        date = LocalDate.now();
    }


    public int totalOrderedProducts() {
        int count = 0;
        for (LineItem lineItem: items) {
            count += lineItem.getQuantity();
        }
        return count;
    }

    public boolean isEmpty() {
        return items.size() == 0;
    }

    public boolean containsProduct(Product product) {
        for (LineItem lineItem: items) {
            if (lineItem.getProduct().getName().equals(product.getName())) {
                return true;
            }
        }
        return false;
    }


    public void increaseQuantity(Product product) {
        for (LineItem lineItem: items) {
            if (lineItem.getProduct().getName().equals(product.getName())) {
                lineItem.add();
                price += product.getDefaultPrice();
            }
        }
    }

    public void updateQuantity() {
        items.removeIf(lineItem -> lineItem.getQuantity() == 0);
    }

    public void addNewItem(LineItem lineItem) {
        items.add(lineItem);
        price += lineItem.getPrice();
    }

    public void addToCart(Product product) {
        if (!isEmpty() && containsProduct(product)) {
            increaseQuantity(product);
        } else {
            addNewItem(new LineItem(product));
        }
        updateQuantity();
        updateCartPrice();
    }

    public void emptyCart() {
        items.clear();
        price = 0;
    }

    public void updateCartPrice() {
        price = 0;
        for (LineItem lineItem: items) {
            price += lineItem.getPrice();
        }
    }

    public void removeProduct(Product product) {
        for (LineItem lineItem: items) {
            if (lineItem.getProduct().getName().equals(product.getName())) {
                lineItem.remove();
                updateQuantity();
                updateCartPrice();
            }
        }
    }
}