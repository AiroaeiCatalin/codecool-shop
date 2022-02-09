package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.jdbc.ProductCategoryDaoJdbc;
import com.codecool.shop.jdbc.ProductDaoJdbc;
import com.codecool.shop.jdbc.SupplierDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.service.ProductService;
import lombok.SneakyThrows;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private final Cart cart = new Cart();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJdbc.getInstance();
        ProductService productService = new ProductService(productDataStore,productCategoryDataStore);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", SupplierDaoJdbc.getInstance().getAll());
        context.setVariable("user", req.getSession().getAttribute("name"));

        System.out.println(cart.getItems());

        setContextVariables(req, context, productService, productDataStore);
        context.setVariable("cartitems", cart);
        req.getSession().setAttribute("cart", cart);



        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String selectedCategory = request.getParameter("select-category");
        String selectedSupplier = request.getParameter("select-supplier");
        request.setAttribute("category", selectedCategory);
        request.setAttribute("supplier", selectedSupplier);
        doGet(request, response);
    }


    private void setContextVariables(HttpServletRequest req, WebContext context, ProductService productService, ProductDao productDataStore) {
        if (req.getAttribute("category") == null && req.getAttribute("supplier") == null) {
            context.setVariable("products", ProductDaoJdbc.getInstance().getAll());
        } else if (req.getAttribute("category").toString().equals("all-categories") && !req.getAttribute("supplier").equals("all-suppliers")) {
            Supplier supplier = SupplierDaoJdbc.getInstance().findByName(req.getAttribute("supplier").toString());
            List<Product> products = SupplierDaoJdbc.getInstance().findBySupplier(supplier.getId());
            context.setVariable("supplier", supplier);
            context.setVariable("products", products);
        } else if (!req.getAttribute("category").toString().equals("all-categories") && req.getAttribute("supplier").equals("all-suppliers")) {
            ProductCategory category = ProductCategoryDaoJdbc.getInstance().findByName(req.getAttribute("category").toString());
            List<Product> products = ProductCategoryDaoJdbc.getInstance().getAllByProductCategory(category.getId());
            context.setVariable("category", category);
            context.setVariable("products", products);
        } else if (req.getAttribute("category").toString().equals("all-categories") && req.getAttribute("supplier").equals("all-suppliers")) {
            List<Product> products = productDataStore.getAll();
            context.setVariable("products", products);
        } else if (!req.getAttribute("category").toString().equals("all-categories") && !req.getAttribute("supplier").equals("all-suppliers")) {
            Supplier supplier = SupplierDaoJdbc.getInstance().findByName(req.getAttribute("supplier").toString());
            ProductCategory category = ProductCategoryDaoJdbc.getInstance().findByName(req.getAttribute("category").toString());
            List<Product> products = ProductCategoryDaoJdbc.getInstance().byProductCategoryAndSupplier(category.getId(), supplier.getId());
            context.setVariable("products", products);
            context.setVariable("category", category);
            context.setVariable("supplier", supplier);
        }
    }
}
