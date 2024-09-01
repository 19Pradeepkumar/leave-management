package com.wavemaker.leavemanagement.services.impl;

import com.wavemaker.leavemanagement.models.Leave;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.models.dto.LeaveTypeDetailsV1;
import com.wavemaker.leavemanagement.repository.MyLeaveRepository;
import com.wavemaker.leavemanagement.repository.impl.MyLeaveRepositoryImpl;
import com.wavemaker.leavemanagement.services.MyLeaveService;

import java.sql.SQLException;
import java.util.List;

public class MyLeaveServiceImpl implements MyLeaveService {
    public MyLeaveRepository myLeaveRepository = new MyLeaveRepositoryImpl();

    public MyLeaveServiceImpl() throws SQLException {
    }

    @Override
    public void applyLeave(Leave leaveObj, int employee_id) throws SQLException {
        myLeaveRepository.applyLeave(leaveObj, employee_id);
    }

    @Override
    public List<LeaveEmployeeV0> getAllEmployeeLeaves(int employeeId) throws SQLException {
        return myLeaveRepository.getAllEmployeeLeaves(employeeId);
    }

    @Override
    public List<LeaveTypeDetailsV1> getAllLeaveSummary(int employeeId) throws SQLException {
        return myLeaveRepository.getAllLeaveSummary(employeeId);
    }

}
