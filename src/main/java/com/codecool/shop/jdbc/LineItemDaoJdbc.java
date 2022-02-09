package com.codecool.shop.jdbc;

import com.codecool.shop.dao.LineItemDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Product;
import com.codecool.shop.utils.DBConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineItemDaoJdbc implements LineItemDao {
    private static LineItemDaoJdbc instance = null;

    private final DataSource dataSource = DBConnection.getInstance().getDataSource();

    private LineItemDaoJdbc() throws SQLException {

    }

    public static LineItemDaoJdbc getInstance() {
        try {
            if (instance ==null) {
                instance = new LineItemDaoJdbc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @Override
    public void add(LineItem lineItem, int userId, int cartId) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO line_item (cart_id, product_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, cartId);
            st.setInt(2, lineItem.getProduct().getId());
            st.setInt(3, lineItem.getQuantity());
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
        } catch (SQLException throwables) {
            throw new RuntimeException("Error while adding new LineItem.", throwables);
        }
    }

    @Override
    public List<LineItem> getLineItems(int cartId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT product_id, quantity FROM line_item WHERE cart_id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, cartId);
            ResultSet rs = st.executeQuery();
            List<LineItem> result = new ArrayList<>();
            while (rs.next()) {
                for (Product product: ProductDaoJdbc.getInstance().getAll()) {
                    if (rs.getInt(1) == product.getId()) {
                        LineItem lineItem = new LineItem(product);
                        lineItem.setQuantity(rs.getInt(2));
                        result.add(lineItem);
                    }
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
