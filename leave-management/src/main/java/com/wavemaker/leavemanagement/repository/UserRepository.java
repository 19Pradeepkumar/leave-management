package com.wavemaker.leavemanagement.repository;


import com.wavemaker.leavemanagement.models.User;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByUsername(String username) throws Exception;

    boolean isManager(String email, HttpSession session) throws SQLException;
}
