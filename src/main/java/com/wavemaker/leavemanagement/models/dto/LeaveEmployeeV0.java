package com.wavemaker.leavemanagement.models.dto;

import com.wavemaker.leavemanagement.models.Leave;


//This view extends Leave model to extend the features like employeeId and employeeName

public class LeaveEmployeeV0 extends Leave {

    private int employeeId;
    private String employeeName;

    public LeaveEmployeeV0(int leaveId, int employeeId, String employeeName, String fromDate, String toDate, String leaveType, String leaveReason, String leaveStatus) {
        super(leaveId, fromDate, toDate, leaveType, leaveReason, leaveStatus);
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}
