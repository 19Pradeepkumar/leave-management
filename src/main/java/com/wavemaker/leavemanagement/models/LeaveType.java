package com.wavemaker.leavemanagement.models;

public class LeaveType {
    private String leaveType;
    private int typeLimit;


    public LeaveType() {}

    public LeaveType(String leaveType, int typeLimit) {
        this.leaveType = leaveType;
        this.typeLimit = typeLimit;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public int getTypeLimit() {
        return typeLimit;
    }

    public void setTypeLimit(int typeLimit) {
        this.typeLimit = typeLimit;
    }

    @Override
    public String toString() {
        return "LeaveType{" +
                "leaveType='" + leaveType + '\'' +
                ", typeLimit=" + typeLimit +
                '}';
    }
}

