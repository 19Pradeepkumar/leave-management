package com.wavemaker.leavemanagement.logout;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    Logger logger = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        //invalidating session
        if (session != null) {
            session.invalidate();
        }

        //removing cookies
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                Cookie cookieToDelete = new Cookie(cookie.getName(), null);
                cookieToDelete.setMaxAge(0);
                cookieToDelete.setPath("/");
                resp.addCookie(cookieToDelete);
            }
        }
        resp.sendRedirect("login.html");
    }
}
