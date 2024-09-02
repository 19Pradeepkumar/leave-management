package com.wavemaker.leavemanagement.models.dto;

import com.wavemaker.leavemanagement.models.LeaveType;

//This view is to extend leaveType along with that to get the details of leaves count

public class LeaveTypeDetailsV1 extends LeaveType {


    private String gender;
    private int used_leaves;
    private int remaining_leaves;

    public LeaveTypeDetailsV1(String gender, String leaveType, int usedLeaves, int remainingLeaves, int totalLeaves) {
        super(leaveType, totalLeaves);
        this.gender = gender;
        this.used_leaves = usedLeaves;
        this.remaining_leaves = remainingLeaves;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUsed_leaves() {
        return used_leaves;
    }

    public void setUsed_leaves(int used_leaves) {
        this.used_leaves = used_leaves;
    }

    public int getRemaining_leaves() {
        return remaining_leaves;
    }

    public void setRemaining_leaves(int remaining_leaves) {
        this.remaining_leaves = remaining_leaves;
    }
}
