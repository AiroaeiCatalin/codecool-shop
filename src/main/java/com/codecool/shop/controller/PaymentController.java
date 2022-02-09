package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.CardDetails;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.OrderStatus;
import com.codecool.shop.utils.AdminLog;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        context.setVariable("cartPrice", cart.getPrice());
        engine.process("product/payment.html", context, response.getWriter());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AdminLog.getInstance().addLog("Customer has filled his card details.");
        CardDetails cardDetails = getCardDetails(request);

        Order order = (Order) request.getSession().getAttribute("order");
        order.setCardDetails(cardDetails);
        order.setOrderStatus(OrderStatus.PAYED);

        request.getSession().setAttribute("order", order);
        response.sendRedirect("/confirmation");
    }

    private CardDetails getCardDetails(HttpServletRequest request) {
        String cardOwner = request.getParameter("card-name");
        String cardNumber = request.getParameter("card-number");
        String month = request.getParameter("month");
        String year = request.getParameter("year");
        return new CardDetails(cardOwner, cardNumber, month, year);
    }
}
