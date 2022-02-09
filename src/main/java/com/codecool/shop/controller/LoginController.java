package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.jdbc.CartDaoJdbc;
import com.codecool.shop.jdbc.UserDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.utils.AdminLog;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/user_login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        PrintWriter out=response.getWriter();
        if (request.getSession().getAttribute("user_id") != null) {
            out.println("You are logged in as " + request.getSession().getAttribute("name"));
            out.close();
        }
        engine.process("product/login.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email-input");
        String password = request.getParameter("password-input");
        if (password.equals(UserDaoJdbc.getInstance().findPassword(email))) {
            AdminLog.getInstance().addLog(UserDaoJdbc.getInstance().find(email).getName() + " has logged in.");
            HttpSession session = request.getSession();
            session.setAttribute("user_id", UserDaoJdbc.getInstance().find(email).getId());
            session.setAttribute("name", UserDaoJdbc.getInstance().find(email).getName());
            session.setAttribute("user_email", UserDaoJdbc.getInstance().find(email));

            //if present, loads saved cart from the db
            loadCart(request);

        }
        response.sendRedirect("/");
    }

    private void loadCart(HttpServletRequest request) {
        int userId = (int) request.getSession().getAttribute("user_id");
        if (CartDaoJdbc.getInstance().getSavedCartId(userId) != 0) {
            int cartId = CartDaoJdbc.getInstance().getSavedCartId(userId);
            Cart loadedCart = CartDaoJdbc.getInstance().getCartWithItems(cartId);
            Cart sessionCart = (Cart) request.getSession().getAttribute("cart");
            sessionCart.emptyCart();
            for (LineItem lineItem: loadedCart.getItems()) {
                sessionCart.addNewItem(lineItem);
            }
            request.getSession().setAttribute("cart", loadedCart);
            AdminLog.getInstance().addLog("Loaded cart from the database.");
        }
    }
}