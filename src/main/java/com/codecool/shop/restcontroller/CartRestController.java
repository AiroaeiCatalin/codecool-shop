package com.codecool.shop.restcontroller;


import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.jdbc.ProductDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.utils.AdminLog;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/cart"})
public class CartRestController extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // can't send Cart obj because it contains other objects
//        Object cartObj =
        Cart itemsCart = (Cart) request.getSession().getAttribute("cart");

        HashMap<String, List<Integer>> cart = new HashMap<>();
        for (LineItem lineItem: itemsCart.getItems()) {
            cart.put(lineItem.getProduct().getName(), List.of(lineItem.getQuantity(), (int) lineItem.getPrice(), (int) lineItem.getProduct().getDefaultPrice(), lineItem.getProduct().getId()));
        }
        String cartString = gson.toJson(cart);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(cartString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
//        Object cartObj =
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String productName = test.substring(test.indexOf(":") + 2, test.lastIndexOf("\""));
        String operation = test.substring(test.indexOf("\"") + 1, test.indexOf(":") - 1);
        if (operation.equals("addition")) {
            AdminLog.getInstance().addLog("Adding one more " + productName + " to cart.");
            cart.addToCart(ProductDaoJdbc.getInstance().findByProdName(productName));
        } else if (operation.equals("subtraction")) {
            AdminLog.getInstance().addLog("Removing one " + productName + " from the cart.");
            cart.removeProduct(ProductDaoJdbc.getInstance().findByProdName(productName));
        }
        cart.updateCartPrice();
    }

}
