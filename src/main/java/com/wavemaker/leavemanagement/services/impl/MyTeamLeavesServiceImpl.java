package com.wavemaker.leavemanagement.services.impl;

import com.wavemaker.leavemanagement.models.dto.EmployeeLeaveDetailsDTO;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.repository.MyTeamLeavesRepository;
import com.wavemaker.leavemanagement.repository.impl.MyTeamLeavesRepositoryImpl;
import com.wavemaker.leavemanagement.services.MyTeamLeavesService;

import java.sql.SQLException;
import java.util.List;

public class MyTeamLeavesServiceImpl implements MyTeamLeavesService {
    public MyTeamLeavesRepository myTeamLeavesRepository = new MyTeamLeavesRepositoryImpl();

    public MyTeamLeavesServiceImpl() throws SQLException {
    }

    @Override
    public List<LeaveEmployeeV0> getAllPendingLeavesForManager(int employeeId) throws SQLException {
        return myTeamLeavesRepository.getAllPendingLeavesForManager(employeeId);
    }

    @Override
    public List<LeaveEmployeeV0> getAllViewLeavesForManager(int employeeId) throws SQLException {
        return myTeamLeavesRepository.getAllViewLeavesForManager(employeeId);
    }

    @Override
    public void leaveApproval(int leaveId, String status) throws SQLException {
        myTeamLeavesRepository.leaveApproval(leaveId,status);
    }

    @Override
    public List<EmployeeLeaveDetailsDTO> getMyTeamLeavesSummary(int employeeId) throws SQLException {
        return myTeamLeavesRepository.getMyTeamLeavesSummary(employeeId);
    }
}
