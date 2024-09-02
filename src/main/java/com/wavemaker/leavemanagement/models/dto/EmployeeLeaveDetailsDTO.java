package com.wavemaker.leavemanagement.models.dto;


import com.wavemaker.leavemanagement.models.Employee;

import java.util.List;

//These DTO is to combine Employee dto and leaveTypeDetails dto in which we get the count of leaves
public class EmployeeLeaveDetailsDTO {
    private Employee employee;
    private List<LeaveTypeDetailsV1> leaveTypeDetails;

    public EmployeeLeaveDetailsDTO(Employee employee, List<LeaveTypeDetailsV1> leaveTypeDetails) {
        this.employee = employee;
        this.leaveTypeDetails = leaveTypeDetails;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<LeaveTypeDetailsV1> getLeaveTypeDetails() {
        return leaveTypeDetails;
    }

    public void setLeaveTypeDetails(List<LeaveTypeDetailsV1> leaveTypeDetails) {
        this.leaveTypeDetails = leaveTypeDetails;
    }

    @Override
    public String toString() {
        return "EmployeeLeaveDetailsDTO{" +
                "employee=" + employee +
                ", leaveTypeDetails=" + leaveTypeDetails +
                '}';
    }
}

