package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.exception.LeaveRepositoryException;
import com.wavemaker.leavemanagement.models.Employee;
import com.wavemaker.leavemanagement.models.dto.EmployeeLeaveDetailsDTO;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.models.dto.LeaveTypeDetailsV1;
import com.wavemaker.leavemanagement.repository.MyTeamLeavesRepository;
import com.wavemaker.leavemanagement.utils.DBConnections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyTeamLeavesRepositoryImpl implements MyTeamLeavesRepository {
    private static final Logger logger = LoggerFactory.getLogger(MyTeamLeavesRepositoryImpl.class);
    private static final String SQL_GET_VIEW_LEAVES_FOR_MANAGER =
            "SELECT L.LEAVE_ID, L.FROM_DATE, L.TO_DATE, L.LEAVE_TYPE, L.LEAVE_REASON, L.LEAVE_STATUS, E.EMPLOYEE_ID, " +
                    "E.EMPLOYEE_NAME " +
                    "FROM LEAVES L JOIN EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID " +
                    "WHERE E.MANAGER_ID = ? AND (L.LEAVE_STATUS = ? OR L.LEAVE_STATUS = ?)";

    private static final String SQL_GET_PENDING_LEAVES_FOR_MANAGER =
            "SELECT L.LEAVE_ID, L.FROM_DATE, L.TO_DATE, L.LEAVE_TYPE, L.LEAVE_REASON, L.LEAVE_STATUS, E.EMPLOYEE_ID, " +
                    "E.EMPLOYEE_NAME " +
                    "FROM LEAVES L JOIN EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID " +
                    "WHERE E.MANAGER_ID = ? AND L.LEAVE_STATUS = ?";

    private static final String SQL_LEAVE_APPROVAL =
            "UPDATE LEAVES SET LEAVE_STATUS = ? WHERE LEAVE_ID = ?";

    private static final String SQL_GET_VIEW_LEAVES_SUMMARY = "SELECT " +
            "e.GENDER AS Gender, e.EMPLOYEE_NAME AS Name, e.EMAIL_ID AS Email, " +
            "e.DATE_OF_BIRTH AS DOB, lt.LEAVE_TYPE AS LeaveType, " +
            "SUM(DATEDIFF(l.TO_DATE, l.FROM_DATE) + 1) AS LeavesUsed, " +
            "(lt.TYPE_LIMIT - IFNULL(SUM(DATEDIFF(l.TO_DATE, l.FROM_DATE) + 1), 0)) AS RemainingLeaves, " +
            "lt.TYPE_LIMIT AS TotalLeaves, e.EMPLOYEE_ID AS employee_id " +
            "FROM EMPLOYEES e " +
            "LEFT JOIN LEAVES l ON e.EMPLOYEE_ID = l.EMPLOYEE_ID " +
            "LEFT JOIN LEAVE_TYPES lt ON l.LEAVE_TYPE = lt.LEAVE_TYPE " +
            "WHERE e.MANAGER_ID = ? " +
            "GROUP BY e.EMPLOYEE_ID, e.EMPLOYEE_NAME, e.EMAIL_ID, e.DATE_OF_BIRTH, lt.LEAVE_TYPE, lt.TYPE_LIMIT " +
            "ORDER BY e.EMPLOYEE_NAME;";


    private final Connection connection = DBConnections.getConnection();

    public MyTeamLeavesRepositoryImpl() throws SQLException {
    }

    @Override
    public List<LeaveEmployeeV0> getAllPendingLeavesForManager(int managerId) throws SQLException {
        List<LeaveEmployeeV0> respList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_PENDING_LEAVES_FOR_MANAGER)) {
            preparedStatement.setInt(1, managerId);
            preparedStatement.setString(2, "PENDING");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int leaveId = resultSet.getInt(1);
                    Date sqlFromDate = resultSet.getDate(2);
                    Date sqlToDate = resultSet.getDate(3);
                    String leaveType = resultSet.getString(4);
                    String leaveReason = resultSet.getString(5);
                    String leaveStatus = resultSet.getString(6);
                    int employeeId = resultSet.getInt(7);
                    String employeeName = resultSet.getString(8);

                    LocalDate localFromDate = sqlFromDate.toLocalDate();
                    LocalDate localToDate = sqlToDate.toLocalDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fromDate = localFromDate.format(formatter);
                    String toDate = localToDate.format(formatter);

                    LeaveEmployeeV0 leaveEmployeeObj = new LeaveEmployeeV0(leaveId, employeeId, employeeName, fromDate,
                            toDate, leaveType, leaveReason, leaveStatus);
                    respList.add(leaveEmployeeObj);
                }
            } catch (Exception e) {
                throw new SQLException("Could not retrieve leaves", e);
            }
        } catch (SQLException e) {
            throw new SQLException("Error in connection", e);
        }

        return respList;
    }

    @Override
    public List<LeaveEmployeeV0> getAllViewLeavesForManager(int managerId) throws SQLException {
        List<LeaveEmployeeV0> respList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_VIEW_LEAVES_FOR_MANAGER)) {
            preparedStatement.setInt(1, managerId);
            preparedStatement.setString(2, "APPROVED");
            preparedStatement.setString(3, "REJECTED");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int leaveId = resultSet.getInt(1);
                    Date sqlFromDate = resultSet.getDate(2);
                    Date sqlToDate = resultSet.getDate(3);
                    String leaveType = resultSet.getString(4);
                    String leaveReason = resultSet.getString(5);
                    String leaveStatus = resultSet.getString(6);
                    int employeeId = resultSet.getInt(7);
                    String employeeName = resultSet.getString(8);

                    LocalDate localFromDate = sqlFromDate.toLocalDate();
                    LocalDate localToDate = sqlToDate.toLocalDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fromDate = localFromDate.format(formatter);
                    String toDate = localToDate.format(formatter);

                    LeaveEmployeeV0 leaveEmployeeObj = new LeaveEmployeeV0(leaveId, employeeId, employeeName, fromDate,
                            toDate, leaveType, leaveReason, leaveStatus);
                    respList.add(leaveEmployeeObj);
                }
            } catch (Exception e) {
                throw new SQLException("Could not retrieve leaves", e);
            }
        } catch (SQLException e) {
            throw new SQLException("Error in connection", e);
        }

        return respList;
    }


    public void leaveApproval(int leaveId, String status) throws SQLException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_LEAVE_APPROVAL)) {
            preparedStatement.setInt(2, leaveId);
            preparedStatement.setString(1, status);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Could not approve leave", e);
            }
        } catch (SQLException e) {
            throw new SQLException("Error in connection", e);
        }
    }

    public List<EmployeeLeaveDetailsDTO> getMyTeamLeavesSummary(int managerId) throws SQLException {
        List<EmployeeLeaveDetailsDTO> respList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_VIEW_LEAVES_SUMMARY)) {
            preparedStatement.setInt(1, managerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<Integer, Employee> employeeMap = new ConcurrentHashMap<>();
                Map<Integer, List<LeaveTypeDetailsV1>> leaveTypeDetailsMap = new HashMap<>();

                while (resultSet.next()) {
                    String gender = resultSet.getString("Gender");
                    String employeeName = resultSet.getString("Name");
                    String employeeEmail = resultSet.getString("Email");
                    Date sqlDob = resultSet.getDate("DOB");
                    LocalDate dob = sqlDob.toLocalDate();
                    String leaveType = resultSet.getString("LeaveType");
                    int leavesUsed = resultSet.getInt("LeavesUsed");
                    int remainingLeaves = resultSet.getInt("RemainingLeaves");
                    int totalLeaves = resultSet.getInt("TotalLeaves");
                    int employeeId = resultSet.getInt("employee_id");

                    Employee employee = new Employee(employeeId, employeeName, employeeEmail, managerId, java.sql.Date.valueOf(dob));
                    employeeMap.putIfAbsent(employeeId, employee);

                    LeaveTypeDetailsV1 leaveTypeDetails = new LeaveTypeDetailsV1(
                            gender,
                            leaveType,
                            leavesUsed,
                            remainingLeaves,
                            totalLeaves
                    );

                    leaveTypeDetailsMap
                            .computeIfAbsent(employeeId, k -> new ArrayList<>())
                            .add(leaveTypeDetails);
                }

                for (Integer employeeId : employeeMap.keySet()) {
                    Employee employee = employeeMap.get(employeeId);
                    List<LeaveTypeDetailsV1> leaveTypeDetailsList = leaveTypeDetailsMap.getOrDefault(employeeId, new ArrayList<>());
                    EmployeeLeaveDetailsDTO dto = new EmployeeLeaveDetailsDTO(employee, leaveTypeDetailsList);
                    respList.add(dto);
                }
            } catch (Exception e) {
                throw new LeaveRepositoryException("Could not insert leave", e);
            }
        } catch (SQLException e) {
            throw new SQLException("Error in connection", e);
        }

        return respList;
    }

}
