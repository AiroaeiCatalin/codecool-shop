package com.codecool.shop.jdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private static ProductCategoryDaoJdbc instance = null;

    private final DataSource dataSource = DBConnection.getInstance().getDataSource();

    private ProductCategoryDaoJdbc() throws SQLException {
    }

    public static ProductCategoryDaoJdbc getInstance() {
        try {
            if (instance == null) {
                instance = new ProductCategoryDaoJdbc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {

    }

    @Override
    public ProductCategory find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, department FROM public.product_category WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            ProductCategory productCategory = new ProductCategory(rs.getString(1), rs.getString(2));
            productCategory.setId(id);
            return productCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<ProductCategory> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, department, id FROM product_category";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<ProductCategory> result = new ArrayList<>();
            while (rs.next()) {
                ProductCategory productCategory = new ProductCategory(rs.getString(1), rs.getString(2));
                productCategory.setId(rs.getInt(3));
                result.add(productCategory);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductCategory findByName(String name) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, department, id FROM product_category WHERE name = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            ProductCategory productCategory = new ProductCategory(rs.getString(1), rs.getString(2));
            productCategory.setId(rs.getInt(3));
            return productCategory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Product> getAllByProductCategory(int productCategoryId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, price, currency, description, product_category_id, supplier_id, id FROM product WHERE product_category_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, productCategoryId);
            ResultSet rs = st.executeQuery();
            List<Product> result = new ArrayList<>();
            while (rs.next()) { // while result set pointer is positioned before or on last row read authors
                Product product = new Product(rs.getString(1), rs.getFloat(2), rs.getString(3),
                        rs.getString(4), ProductCategoryDaoJdbc.getInstance().find(rs.getInt(5)), SupplierDaoJdbc.getInstance().find(rs.getInt(6)));
                product.setId(rs.getInt(7));
                result.add(product);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all products", e);
        }
    }

    public List<Product> byProductCategoryAndSupplier(int productCategoryId, int supplierId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, price, currency, description, supplier_id, product_category_id, id FROM product WHERE product_category_id = ? AND supplier_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, productCategoryId);
            st.setInt(2, supplierId);
            ResultSet rs = st.executeQuery();
            List<Product> result = new ArrayList<>();
            while (rs.next()) { // while result set pointer is positioned before or on last row read authors
                Product product = new Product(rs.getString(1), rs.getFloat(2), rs.getString(3),
                        rs.getString(4), ProductCategoryDaoJdbc.getInstance().find(rs.getInt(5)), SupplierDaoJdbc.getInstance().find(rs.getInt(6)));
                product.setId(rs.getInt(7));
                result.add(product);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all products", e);
        }
    }
}
