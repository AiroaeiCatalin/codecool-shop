package com.codecool.shop.jdbc;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.ShippingInfo;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.*;

public class UserDaoJdbc implements UserDao {
    private static UserDaoJdbc instance = null;
    private final DataSource dataSource = DBConnection.getInstance().getDataSource();

    private UserDaoJdbc() throws SQLException {

    }

    public static UserDaoJdbc getInstance() {
        try {
            if (instance == null) {
                instance = new UserDaoJdbc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void add(User user) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO \"user\" (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, user.getName());
            st.setString(2, user.getEmail());
            st.setString(3, user.getPassword());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while adding new Author.", throwables);
        }
    }

    @Override
    public User find(String email) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT name, email, password, id FROM \"user\" WHERE email = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            User user = new User(rs.getString(1), rs.getString(2), rs.getString(3));
            user.setId(rs.getInt(4));
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findPassword(String inputEmail) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT password FROM public.user WHERE email = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, inputEmail);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rs.getString("password");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveShippingInfo(int userId, String name, String email, String phoneNumber, String billingAddress, String shippingAddress, String country, String city, String zipcode) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO shipping_info (user_id, name, email, phonenumber, billing_address, shipping_address, zipcode, country, city) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, userId);
            st.setString(2, name);
            st.setString(3, email);
            st.setString(4, phoneNumber);
            st.setString(5, billingAddress);
            st.setString(6, shippingAddress);
            st.setString(7, zipcode);
            st.setString(8, country);
            st.setString(9, city);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next(); // Read next returned value - in this case the first one. See ResultSet docs for more explaination
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while adding new Shpping info.", throwables);
        }
    }

    @Override
    public ShippingInfo findShippingInfo(int userId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM shipping_info WHERE user_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return new ShippingInfo(rs.getInt("user_id"), rs.getString("email"), rs.getString("name")
                    ,rs.getString("phonenumber"), rs.getString("billing_address"),
                    rs.getString("shipping_address"), rs.getString("zipcode"), rs.getString("country"), rs.getString("city"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

