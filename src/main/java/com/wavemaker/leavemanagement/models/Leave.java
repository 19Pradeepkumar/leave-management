package com.wavemaker.leavemanagement.models;

public class Leave {
    private int leaveId;
    private String fromDate;
    private String toDate;
    private String leaveType;
    private String leaveReason;
    private String leaveStatus;

    public Leave() {
    }



    public Leave( int leaveId, String fromDate, String toDate, String leaveType, String leaveReason, String leaveStatus) {
        this.leaveId=leaveId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveType = leaveType;
        this.leaveReason = leaveReason;
        this.leaveStatus = leaveStatus;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }
//    public int getEmployeeId() {
//        return employeeId;
//    }
//
//    public void setEmployeeId(int employeeId) {
//        this.employeeId = employeeId;
//    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    // toString method
    @Override
    public String toString() {
        return "Leave{" +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", leaveType='" + leaveType + '\'' +
                ", leaveReason='" + leaveReason + '\'' +
                ", leaveStatus='" + leaveStatus + '\'' +
                '}';
    }
}
