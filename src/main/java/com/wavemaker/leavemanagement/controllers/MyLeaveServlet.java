package com.wavemaker.leavemanagement.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.exception.DataProcessingException;
import com.wavemaker.leavemanagement.models.Leave;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.models.dto.LeaveTypeDetailsV1;
import com.wavemaker.leavemanagement.services.MyLeaveService;
import com.wavemaker.leavemanagement.services.impl.MyLeaveServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@WebServlet("/myleaves")
public class MyLeaveServlet extends HttpServlet {
    public static Logger logger = LoggerFactory.getLogger(MyLeaveServlet.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    public MyLeaveService myLeaveService = new MyLeaveServiceImpl();


    public MyLeaveServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        List<LeaveEmployeeV0> responseList = null;
        List<LeaveTypeDetailsV1> responseListSummary = null;
        String action = req.getParameter("action");

        int employee_id = (int) session.getAttribute("userId");

        if (employee_id == 0) {
            resp.sendRedirect("/login.html");
        }
        String json = "";
        try {
            if (Objects.equals(action, "myLeavesStatus")) {
                //To get all user leaves status

                responseList = myLeaveService.getAllEmployeeLeaves(employee_id);
                json = objectMapper.writeValueAsString(responseList);

            } else if (Objects.equals(action, "myLeavesSummary")) {
                //Number of  leaves avalible and used for user

                responseListSummary = myLeaveService.getAllLeaveSummary(employee_id);
                json = objectMapper.writeValueAsString(responseListSummary);

            }
        } catch (SQLException e) {
            throw new DataProcessingException("Error processing data", e);
        }
        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Leave leaveObj = null;
        int employee_id = (int) session.getAttribute("userId");
        String action = req.getParameter("action");

        //action used to indiacte this is for apply leave
        if ("apply_leave".equals(action)) {
            try {
                leaveObj = objectMapper.readValue(req.getReader(), Leave.class);
                myLeaveService.applyLeave(leaveObj, employee_id);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(objectMapper.writeValueAsString("leave applied"));
            } catch (SQLException e) {

                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new DataProcessingException("Error processing data", e);

            } catch (Exception e) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid data format.");

            }
        }
    }


}
