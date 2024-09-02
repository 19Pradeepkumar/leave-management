package com.wavemaker.leavemanagement.services;

import com.wavemaker.leavemanagement.models.dto.EmployeeLeaveDetailsDTO;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;

import java.sql.SQLException;
import java.util.List;

public interface MyTeamLeavesService {
    List<LeaveEmployeeV0> getAllPendingLeavesForManager(int employeeId) throws SQLException;
    List<LeaveEmployeeV0> getAllViewLeavesForManager(int employeeId) throws SQLException;
    void leaveApproval(int leaveId, String status) throws SQLException;
    List<EmployeeLeaveDetailsDTO> getMyTeamLeavesSummary(int employeeId) throws SQLException;
}
