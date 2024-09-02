package com.wavemaker.leavemanagement.services;

import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.models.Leave;
import com.wavemaker.leavemanagement.models.dto.LeaveTypeDetailsV1;

import java.sql.SQLException;
import java.util.List;

public interface MyLeaveService {
    void applyLeave(Leave leaveObj, int employee_id) throws SQLException;
    List<LeaveEmployeeV0> getAllEmployeeLeaves(int employeeId) throws SQLException;
    List<LeaveTypeDetailsV1> getAllLeaveSummary(int employeeId) throws SQLException;
}
