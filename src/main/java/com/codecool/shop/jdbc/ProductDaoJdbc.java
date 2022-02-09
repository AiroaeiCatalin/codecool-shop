package com.codecool.shop.jdbc;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJdbc implements ProductDao {
    private static ProductDaoJdbc instance = null;

    private final DataSource dataSource = DBConnection.getInstance().getDataSource();
    private ProductDaoJdbc() throws SQLException {

    }

    public static ProductDaoJdbc getInstance() {
        try {
            if (instance == null) {
                instance = new ProductDaoJdbc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void add(Product product) {

    }

    @Override
    public Product find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, price, currency, description, product_category_id, supplier_id FROM public.product WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Product product = new Product(rs.getString(1), rs.getFloat(2), rs.getString(3),
                    rs.getString(4), ProductCategoryDaoJdbc.getInstance().find(rs.getInt(5)), SupplierDaoJdbc.getInstance().find(rs.getInt(6)));
            product.setId(id);
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public Product findByProdName(String productName) {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println(productName);
            String sql = "SELECT name, price, currency, description, product_category_id, supplier_id,  id  FROM public.product WHERE name = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, productName);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Product product = new Product(rs.getString(1), rs.getFloat(2), rs.getString(3),
                    rs.getString(4), ProductCategoryDaoJdbc.getInstance().find(rs.getInt(5)), SupplierDaoJdbc.getInstance().find(rs.getInt(6)));
            product.setId(rs.getInt("id"));
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, price, currency, description, product_category_id,  supplier_id, id FROM product";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<Product> result = new ArrayList<>();
            while (rs.next()) { // while result set pointer is positioned before or on last row read authors
                Product product = new Product(rs.getString(1), rs.getFloat(2), rs.getString(3),
                        rs.getString(4), ProductCategoryDaoJdbc.getInstance().find(rs.getInt(5)), SupplierDaoJdbc.getInstance().find(rs.getInt(6)));
                product.setId(rs.getInt(7));
                result.add(product);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all authors", e);
        }
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return null;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return null;
    }

}
