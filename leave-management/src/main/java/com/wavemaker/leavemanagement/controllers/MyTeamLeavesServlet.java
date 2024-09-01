package com.wavemaker.leavemanagement.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.exception.DataProcessingException;
import com.wavemaker.leavemanagement.models.dto.EmployeeLeaveDetailsDTO;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.services.MyTeamLeavesService;
import com.wavemaker.leavemanagement.services.impl.MyTeamLeavesServiceImpl;
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

@WebServlet("/myteamleaves")
public class MyTeamLeavesServlet extends HttpServlet {

    public static Logger logger = LoggerFactory.getLogger(MyLeaveServlet.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    public MyTeamLeavesService myTeamLeavesService = new MyTeamLeavesServiceImpl();

    public MyTeamLeavesServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<LeaveEmployeeV0> responseList = null;
        List<EmployeeLeaveDetailsDTO> responseList1 = null;

        HttpSession session = req.getSession(false);

        String action = req.getParameter("action");
        int employee_id = (int) session.getAttribute("userId");
        String json = "";

        try {
            //to get all the team leave requests through action mentioned in url
            if ("myTeamLeavesRequest".equals(action)) {
                responseList = myTeamLeavesService.getAllPendingLeavesForManager(employee_id);
                json = objectMapper.writeValueAsString(responseList);
            }
            //to get all the team leaves for viewing
            else if ("myTeamLeavesView".equals(action)) {
                responseList = myTeamLeavesService.getAllViewLeavesForManager(employee_id);
                json = objectMapper.writeValueAsString(responseList);
            }
            // to get all the team members type of leaves summary
            else if ("myTeamLeavesSummary".equals(action)) {
                logger.info("in leave summary");
                responseList1 = myTeamLeavesService.getMyTeamLeavesSummary(employee_id);
                json = objectMapper.writeValueAsString(responseList1);
            }
        }
        catch (SQLException e) {
            throw new DataProcessingException("Error processing data", e);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(json);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Unauthorized");
            return;
        }

        try {
            // Read JSON data from the request body
            JsonNode jsonNode = objectMapper.readTree(req.getInputStream());

            // Extract data from JSON
            int leaveId = jsonNode.get("leaveId").asInt();
            String status = jsonNode.get("status").asText();

            // Process the leave approval
            myTeamLeavesService.leaveApproval(leaveId, status);

            // Respond with success
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString("Leave request processed"));

        } catch (Exception e) {

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid operation");

        }
    }

}
