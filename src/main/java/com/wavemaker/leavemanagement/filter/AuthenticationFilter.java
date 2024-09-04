package com.wavemaker.leavemanagement.filter;

import com.wavemaker.leavemanagement.exception.AuthenticationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

import static com.wavemaker.leavemanagement.repository.impl.UserRepositoryImpl.sessionMap;

//Filter for my-leaves and my-team-leaves
@WebFilter(urlPatterns = {"/myleaves", "/myteamleaves"})

public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
            IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        int userId = 0;

        Cookie[] cookies = httpServletRequest.getCookies();
        HttpSession session = httpServletRequest.getSession(false);

        //checking cookies whether a cookie is existing or not
        if (session != null) {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("JSESSIONID".equals(cookie.getName())) {
                        try {
                            if (Objects.equals(cookie.getValue(), session.getId())) {
                                userId = sessionMap.get(session.getId());
                                session.setAttribute("userId", userId);
                                break;
                            }
                        } catch (Exception e) {
                            logger.error("Error validating session", e);
                            throw new AuthenticationException("Error fetching userId from session", e);
                        }
                    }
                }
            }
        }

        if (userId == 0) {
            logger.info(" not  a valid user");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
