package com.wavemaker.leavemanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.models.Holidays;
import com.wavemaker.leavemanagement.services.HolidayDetailsService;
import com.wavemaker.leavemanagement.services.impl.HolidayDetailsServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/holidays")
public class HolidayDetailsServlet extends HttpServlet {
    public HolidayDetailsService holidayDetailsService = new HolidayDetailsServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HolidayDetailsServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Holidays> respList = null;

        try{
            respList = holidayDetailsService.getAllHoildays();
            String json = objectMapper.writeValueAsString(respList);
            resp.getWriter().write(json);
        }
        catch (Exception e){

            resp.setStatus(500);
        }
    }
}
