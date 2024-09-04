package com.wavemaker.leavemanagement.models;

public class Holidays {
    private String holidayName;
    private  String Date;
    private String holidayType;

    public Holidays(String holidayName, String holidayDate, String holidayType) {
        this.holidayName = holidayName;
        this.Date = holidayDate;
        this.holidayType = holidayType;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String type) {
        this.holidayType = type;
    }


}
