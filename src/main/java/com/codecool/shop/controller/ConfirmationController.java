package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.jdbc.CartDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.OrderStatus;
import com.codecool.shop.utils.AdminLog;
import com.codecool.shop.utils.Utils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/confirmation"})
public class ConfirmationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        Order order = (Order) request.getSession().getAttribute("order");

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        context.setVariable("order", order);

        context.setVariable("lineitems", cart.getItems());


        context.setVariable("tax", cart.getPrice() * 0.1);

        CartDaoJdbc.getInstance().addCart((int) request.getSession().getAttribute("user_id"), cart);

        AdminLog.getInstance().addLog("Order has been completed");
        AdminLog.getInstance().writeLogsToFile(order);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Utils.writeOrderToFile(order);

        engine.process("product/confirmation.html", context, response.getWriter());

//        request.getSession().invalidate();

//        request.getSession().setAttribute("cart", new Cart());


        cart.emptyCart();
    }
}
