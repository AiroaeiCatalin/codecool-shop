package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.jdbc.CartDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.utils.AdminLog;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/profile"})
public class UserProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("cartOrders", getOrders(request));
        context.setVariable("user", request.getSession().getAttribute("name"));
        engine.process("product/profile.html", context, response.getWriter());
    }


    private List<Cart> getOrders(HttpServletRequest request) {
        int userId = (int) request.getSession().getAttribute("user_id");
        List<Integer> userCartIds = CartDaoJdbc.getInstance().getCartsForUser(userId);
        List<Cart> ordersWithItems = new ArrayList<>();
        int dayCounter = 1;
        for(Integer cartId: userCartIds) {
            Cart cart = CartDaoJdbc.getInstance().getCartWithItems(cartId);
            cart.setDate(LocalDate.of(2021, 2, dayCounter));
            cart.setId(dayCounter);
            dayCounter += 5;
            ordersWithItems.add(cart);
        }
        AdminLog.getInstance().addLog("Display order history for user with id: " + userId);
        return ordersWithItems;
    }

}
