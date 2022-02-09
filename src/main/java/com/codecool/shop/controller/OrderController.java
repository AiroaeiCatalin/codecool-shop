package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.jdbc.OrderDaoJdbc;
import com.codecool.shop.jdbc.UserDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.utils.AdminLog;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = {"/checkout"})
public class OrderController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        if (request.getSession().getAttribute("user_id") != null) {
            int loggedInUserId = Integer.parseInt(request.getSession().getAttribute("user_id").toString());
            if (UserDaoJdbc.getInstance().findShippingInfo(loggedInUserId) != null) {
                context.setVariable("shippingInfo", UserDaoJdbc.getInstance().findShippingInfo(loggedInUserId));
            }
        }
        context.setVariable("totalprice", cart.getPrice());
        context.setVariable("lineitems", cart.getItems());


        engine.process("product/billingdetails.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Order order = createOrder(request);

        if (request.getSession().getAttribute("user_id") != null) {
            System.out.println(order);
            System.out.println(request.getSession().getAttribute("user_id"));
            OrderDaoJdbc.getInstance().add(order, (int) request.getSession().getAttribute("user_id"));
        }

        AdminLog.getInstance().addLog("Order has been created.");

        // sends the order object to the payment servlet
        request.getSession().setAttribute("order", order);
        response.sendRedirect("/payment");
    }

    private Order createOrder(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone-number");
        String billingAddress = request.getParameter("billing-address");
        String shippingAddress = request.getParameter("shipping-address");
        String country = request.getParameter("country");
        String zipcode = request.getParameter("zipcode");

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        return new Order(cart, cart.getPrice(), name, email, phoneNumber, billingAddress, shippingAddress, country, zipcode);
    }


}
