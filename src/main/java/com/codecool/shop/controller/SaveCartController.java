package com.codecool.shop.controller;

import com.codecool.shop.jdbc.CartDaoJdbc;
import com.codecool.shop.model.Cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/save_shopping_cart"})
public class SaveCartController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user_id") == null) {
            PrintWriter out=response.getWriter();
            out.println("You are not logged in! Please log in.");
            out.close();
        } else {
            CartDaoJdbc.getInstance().addCartToSavedCarts((Integer) request.getSession().getAttribute("user_id"), (Cart) request.getSession().getAttribute("cart"));
            response.sendRedirect("/cart-items");
        }
    }
}
