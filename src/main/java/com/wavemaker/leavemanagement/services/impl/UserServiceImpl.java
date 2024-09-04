package com.wavemaker.leavemanagement.services.impl;

import com.wavemaker.leavemanagement.models.User;
import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.repository.impl.UserRepositoryImpl;
import com.wavemaker.leavemanagement.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository = new UserRepositoryImpl();

    public UserServiceImpl() throws SQLException {

    }

    //Validating the user and password with details given by user and in the storage
    @Override
    public String login(String username, String password) throws Exception {
        try {
            Optional<User> userOptional = userRepository.getUserByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getUserPassword().equals(password)) {
                    return user.getUserEmail();
                } else {
                    throw new Exception("Invalid password");
                }
            } else {
                throw new Exception("User not found");
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            throw new Exception("Login failed", e);
        }
    }

    @Override
    public boolean isManager(String email, HttpSession session) throws SQLException {
        return userRepository.isManager(email, session);
    }
}
