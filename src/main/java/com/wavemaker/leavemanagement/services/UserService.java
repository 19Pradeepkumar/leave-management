package com.wavemaker.leavemanagement.services;

import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

public interface UserService {
    String login(String username, String password) throws Exception;

    boolean isManager(String email, HttpSession session) throws SQLException;
}
