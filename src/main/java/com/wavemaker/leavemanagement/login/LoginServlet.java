package com.wavemaker.leavemanagement.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.services.UserService;
import com.wavemaker.leavemanagement.services.impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginServlet() throws SQLException {

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        try {
            Map<String, String> loginData = objectMapper.readValue(req.getReader(), Map.class);
            String username = loginData.get("username");
            String password = loginData.get("password");
            String Email = userService.login(username, password);
            if (Email != null) {
                Cookie cookie = new Cookie("JSESSIONID", session.getId());
                cookie.setPath("/");
                boolean isManager = userService.isManager(Email, session);
                logger.info(String.valueOf(isManager));
                Map<String, Object> data = new HashMap<>();
                data.put("message", isManager);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.addCookie(cookie);

                String json = objectMapper.writeValueAsString(data);
                resp.getWriter().write(json);
                resp.setStatus(200);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Invalid username or password");
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Error parsing login details: " + e.getMessage());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("An unexpected error occurred: " + e.getMessage());
        }
    }

}
