package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.jdbc.UserDaoJdbc;
import com.codecool.shop.utils.AdminLog;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/save_billing_details"})
public class SaveBillingDetailsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());

        engine.process("product/save_details.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String zipcode = request.getParameter("zipcode");
        String billingAddress = request.getParameter("billing_address");
        String email = request.getParameter("email");
        String shippingAddress = request.getParameter("shipping_address");
        String phoneNumber = request.getParameter("phone_number");
        if (request.getSession().getAttribute("user_id") != null) {
            UserDaoJdbc.getInstance().saveShippingInfo((int) request.getSession().getAttribute("user_id"),name, email, phoneNumber, billingAddress, shippingAddress, country, city, zipcode);
            AdminLog.getInstance().addLog("Saving shipping details.");
            response.sendRedirect("/checkout");
        } else {
            PrintWriter out = response.getWriter();
            out.println("You are not logged in.");
            out.close();
        }
    }
}
