package com.codecool.shop.jdbc;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.OrderStatus;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJdbc implements OrderDao {
    private static OrderDaoJdbc instance = null;

    private final DataSource dataSource = DBConnection.getInstance().getDataSource();

    private OrderDaoJdbc() throws SQLException {
    }

    public static OrderDaoJdbc getInstance() {
        try{
            if(instance==null){
                instance = new OrderDaoJdbc();
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void add(Order order, int userId) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO \"order\" (price, name, email, billing_address, status, user_id, country) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setFloat(1, order.getPrice());
            st.setString(2, order.getName());
            st.setString(3, order.getEmail());
            st.setString(4, order.getBillingAddress());
            st.setString(5, order.getOrderStatus().toString());
            st.setInt(6, userId);
            st.setString(7, order.getCountry());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();

        } catch (SQLException throwables) {
            throw new RuntimeException("Error with adding an order");
        }
    }

    @Override
    public void updateOrder(String name, OrderStatus newStatus) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "UPDATE order SET status = ? WHERE name = ?";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, newStatus.toString());
            st.setString(2, name);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllByUser(int userId) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "SELECT * FROM order WHERE user_id = ?";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, userId);
            ResultSet rs = st.getGeneratedKeys();
            List<Order> result = new ArrayList<>();
            while(rs.next()) {
                Order order = new Order(rs.getInt(1), (float) rs.getDouble(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), OrderStatus.valueOf(rs.getString(6)), rs.getInt(7), rs.getString(8));
                result.add(order);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all orders by user", e);
        }
    }
}
