package com.wavemaker.leavemanagement.models;

import java.util.Date;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String employeeEmail;
    private int managerId;
    private Date dateOfBirth ;

    public Employee(int employeeId, String employeeName, String employeeEmail, int managerId, Date dateOfBirth) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.managerId = managerId;
        this.dateOfBirth = dateOfBirth;
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

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeEmail='" + employeeEmail + '\'' +
                ", managerId=" + managerId +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
