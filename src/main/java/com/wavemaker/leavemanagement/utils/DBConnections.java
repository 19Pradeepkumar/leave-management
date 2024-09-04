package com.wavemaker.leavemanagement.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnections {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/leave_management";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    private static Logger logger = LoggerFactory.getLogger(DBConnections.class);

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            logger.info("Connected to the database");

        } catch (SQLException e) {
            logger.error("SQL error occurred while connecting to the database", e);
            throw e;
        }
        return connection;
    }

}
