package com.codecool.shop.jdbc;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.*;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoJdbc implements CartDao {
    private static CartDaoJdbc instance = null;

    private final DataSource dataSource = DBConnection.getInstance().getDataSource();
    private CartDaoJdbc() throws SQLException {

    }

    public static CartDaoJdbc getInstance() {
        try {
            if (instance == null) {
                instance = new CartDaoJdbc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void addCart(int userId, Cart cart) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO cart (user_id) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            addCartLineItems(cart, userId, generatedKey);
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating a new cart.");
        }
    }

    @Override
    public void addCartToSavedCarts(int userId, Cart cart) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO saved_cart (user_id) VALUES (?)";
            PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            addCartLineItems(cart, userId, generatedKey);
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating a new cart.");
        }
    }
//
//    @Override
//    public int getCartId(int userId) {
//        try (Connection conn = dataSource.getConnection()) {
//            String sql = "SELECT id, user_id FROM cart WHERE user_id = ? ORDER BY id DESC LIMIT 1";
//            PreparedStatement st = conn.prepareStatement(sql);
//            st.setInt(1, userId);
//            ResultSet rs = st.executeQuery();
//            if (!rs.next()) {
//                return 0;
//            }
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    @Override
    public int getSavedCartId(int userId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, user_id FROM saved_cart WHERE user_id = ? ORDER BY id DESC LIMIT 1";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getCartsForUser(int userId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM cart WHERE user_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, userId);
            List<Integer> result = new ArrayList<>();
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("id"));;
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all carts", e);
        }
    }

    @Override
    public Cart getCartWithItems(int cartId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM line_item WHERE cart_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, cartId);
            ResultSet rs = st.executeQuery();
            Cart cart = new Cart();
            while (rs.next()) {
                for (int i = 0; i < rs.getInt("quantity"); i++) {
                    cart.addToCart(ProductDaoJdbc.getInstance().find(rs.getInt("product_id")));
                }
            }
            return cart;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all carts", e);
        }
    }


    private void addCartLineItems(Cart cart, int userid, int cartId) {
        if (cart.getItems().size() > 0) {
            for (LineItem lineItem: cart.getItems()) {
                LineItemDaoJdbc.getInstance().add(lineItem, userid, cartId);
            }
        } else {
            throw new IllegalArgumentException("Cannot save any items - cart is empty!");
        }
    }
}

