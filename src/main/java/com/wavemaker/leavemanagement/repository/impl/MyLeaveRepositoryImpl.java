package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.exception.LeaveRepositoryException;
import com.wavemaker.leavemanagement.models.Leave;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.models.dto.LeaveTypeDetailsV1;
import com.wavemaker.leavemanagement.repository.MyLeaveRepository;
import com.wavemaker.leavemanagement.utils.DBConnections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MyLeaveRepositoryImpl implements MyLeaveRepository {
    private static final Logger logger = LoggerFactory.getLogger(MyLeaveRepositoryImpl.class);
    private static final String SQL_APPLY_LEAVE =
            "INSERT INTO LEAVES (EMPLOYEE_ID, FROM_DATE, TO_DATE, LEAVE_TYPE, LEAVE_REASON, LEAVE_STATUS) VALUES (?,?,?," +
                    "?,?,?);";


    private static final String SQL_GET_ALL_EMPLOYEE_LEAVES =
            "SELECT L.LEAVE_ID, L.FROM_DATE, L.TO_DATE, L.LEAVE_TYPE, L.LEAVE_REASON, L.LEAVE_STATUS, E.EMPLOYEE_NAME " +
                    "FROM LEAVES L JOIN EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID " +
                    "WHERE E.EMPLOYEE_ID = ?";

    private static final String SQL_GET_ALL_LEAVE_TYPE_DETAILS =
            "SELECT\n" +
                    "    lt.LEAVE_TYPE,\n" +
                    "    IFNULL(SUM(DATEDIFF(l.TO_DATE, l.FROM_DATE) + 1), 0) AS used_leaves,\n" +
                    "    lt.TYPE_LIMIT - IFNULL(SUM(DATEDIFF(l.TO_DATE, l.FROM_DATE) + 1), 0) AS remaining_leaves,\n" +
                    "    lt.TYPE_LIMIT AS total_leaves\n" +
                    "FROM\n" +
                    "    LEAVE_TYPES lt\n" +
                    "LEFT JOIN\n" +
                    "    LEAVES l ON lt.LEAVE_TYPE = l.LEAVE_TYPE \n" +
                    "    AND l.EMPLOYEE_ID = ? \n" +
                    "    AND l.LEAVE_STATUS = 'Approved'\n" +
                    "GROUP BY\n" +
                    "    lt.LEAVE_TYPE, lt.TYPE_LIMIT";

    private static final String SQL_GET_GENDER = "SELECT GENDER FROM EMPLOYEES WHERE EMPLOYEE_ID=? ";


    private final Connection connection = DBConnections.getConnection();

    public MyLeaveRepositoryImpl() throws SQLException {
    }

    @Override
    public void applyLeave(Leave leaveObj, int employee_id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_APPLY_LEAVE)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fromDate = LocalDate.parse(leaveObj.getFromDate(), dateFormatter);
            LocalDate toDate = LocalDate.parse(leaveObj.getFromDate(), dateFormatter);

            preparedStatement.setInt(1, employee_id);
            preparedStatement.setDate(2, Date.valueOf(fromDate));
            preparedStatement.setDate(3, Date.valueOf(toDate));
            preparedStatement.setString(4, leaveObj.getLeaveType());
            preparedStatement.setString(5, leaveObj.getLeaveReason());
            preparedStatement.setString(6, leaveObj.getLeaveStatus());

            try {
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new LeaveRepositoryException("Could not insert leave", e);
            }

        } catch (Exception e) {
            throw new SQLException("Error in connection", e);
        }
    }

    @Override
    public List<LeaveEmployeeV0> getAllEmployeeLeaves(int employeeId) throws SQLException {
        List<LeaveEmployeeV0> respList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_EMPLOYEE_LEAVES)) {
            preparedStatement.setInt(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int leaveId = resultSet.getInt(1);
                    Date sqlFromDate = resultSet.getDate(2);
                    Date sqlToDate = resultSet.getDate(3);
                    String leaveType = resultSet.getString(4);
                    String leaveReason = resultSet.getString(5);
                    String leaveStatus = resultSet.getString(6);
                    String employeeName = resultSet.getString(7);

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
                throw new LeaveRepositoryException("Could not insert leave", e);
            }
        } catch (SQLException e) {
            throw new SQLException("Error in connection", e);
        }

        return respList;
    }

    @Override
    public List<LeaveTypeDetailsV1> getAllLeaveSummary(int employeeId) throws SQLException {
        List<LeaveTypeDetailsV1> respList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_LEAVE_TYPE_DETAILS)) {
            preparedStatement.setInt(1, employeeId);

            PreparedStatement preparedStatement1 = connection.prepareStatement(SQL_GET_GENDER);
            preparedStatement1.setInt(1, employeeId);

            ResultSet resultSet1 = preparedStatement1.executeQuery();
            String gender = "";
            if (resultSet1.next()) {
                gender = resultSet1.getString(1);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String leaveType = resultSet.getString(1);
                    int used_leaves = resultSet.getInt(2);
                    int remaining_leaves = resultSet.getInt(3);
                    int total_leaves = resultSet.getInt(4);


                    LeaveTypeDetailsV1 leaveTypeEmployeeObj = new LeaveTypeDetailsV1(gender, leaveType, used_leaves, remaining_leaves, total_leaves);
                    respList.add(leaveTypeEmployeeObj);
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

