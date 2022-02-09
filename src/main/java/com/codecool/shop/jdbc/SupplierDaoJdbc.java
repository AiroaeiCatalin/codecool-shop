package com.codecool.shop.jdbc;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao {
    private static SupplierDaoJdbc instance = null;

    private final DataSource dataSource = DBConnection.getInstance().getDataSource();

    private SupplierDaoJdbc() throws SQLException {

    }

    public static SupplierDaoJdbc getInstance()  {
        try {
            if (instance == null) {
                instance = new SupplierDaoJdbc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {

    }

    @Override
    public Supplier find(int id) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, description FROM public.supplier WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Supplier supplier = new Supplier(rs.getString(1), rs.getString(2));
            System.out.println(supplier);
            supplier.setId(id);
            return supplier;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Supplier> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, description, id FROM public.supplier";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<Supplier> result = new ArrayList<>();
            while (rs.next()) {
                Supplier supplier = new Supplier(rs.getString(1), rs.getString(2));
                supplier.setId(rs.getInt(3));
                result.add(supplier);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Supplier findByName(String name) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, description, id FROM supplier WHERE name = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Supplier supplier = new Supplier(rs.getString(1), rs.getString(2));
            supplier.setId(rs.getInt(3));
            return supplier;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findBySupplier(int supplierId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, price, currency, description, product_category_id, supplier_id, id FROM product WHERE supplier_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, supplierId);
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
