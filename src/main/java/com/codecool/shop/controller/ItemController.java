package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.jdbc.ProductDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.AdminLog;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/add-item")
public class ItemController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        AdminLog.getInstance().addLog("Fetching cart quantity.");

        Integer cartQuantity = getCart(req).totalOrderedProducts();

        Gson gson = new Gson();
        String quantityJson = gson.toJson(cartQuantity);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(quantityJson);
        out.flush();
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jsonObj = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String productName = jsonObj.substring(jsonObj.indexOf(":") + 2, jsonObj.lastIndexOf("\""));
        AdminLog.getInstance().addLog("Added " + productName + " to cart.");
        Product product = ProductDaoJdbc.getInstance().findByProdName(productName);

        System.out.println(product);
        Cart cart = getCart(request);

        cart.addToCart(product);

    }

    private Cart getCart(HttpServletRequest request) {
        Object cartObj = request.getSession().getAttribute("cart");
        System.out.println(cartObj);
        return (Cart) cartObj;
    }
}
