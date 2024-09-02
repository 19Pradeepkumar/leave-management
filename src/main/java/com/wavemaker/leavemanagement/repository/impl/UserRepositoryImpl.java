package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.models.User;
import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.utils.DBConnections;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class UserRepositoryImpl implements UserRepository {
    private final Connection connection = DBConnections.getConnection();
    public static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    public static final Map<String,Integer> sessionMap=new HashMap<>();

    public UserRepositoryImpl() throws SQLException {

    }

    //to retrieve the details of the username and password from the storage
    @Override
    public Optional<User> getUserByUsername(String username) throws Exception {
        String sql = "SELECT * FROM USERS WHERE EMAIL_ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getString("EMAIL_ID"),
                        rs.getString("USER_PASSWORD")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw new Exception("Could not retrieve user from database", e);
        }
        return Optional.empty();
    }

    //this is to check the user whether he is a manager for any employee or not
    public boolean isManager(String emailID, HttpSession session) throws SQLException {
        Connection connection = DBConnections.getConnection();
        String query = "SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, emailID);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int employeeID = resultSet.getInt(1);
            sessionMap.put(session.getId(), employeeID);
            query = "SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE MANAGER_ID = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, employeeID);

            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        return false;
    }
}

