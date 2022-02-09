package com.codecool.shop.utils;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance = null;

    private final DataSource dataSource = connect();

    private DBConnection() throws SQLException {

    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public DataSource connect() throws SQLException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setDatabaseName("codecoolshop");
        dataSource.setUser("Catalin");
        dataSource.setPassword("1214");


        System.out.println("Trying to connect...");
        dataSource.getConnection().close();
        System.out.println("Connection OK");
        AdminLog.getInstance().addLog("DB Connection OK");

        return dataSource;
    }

}

