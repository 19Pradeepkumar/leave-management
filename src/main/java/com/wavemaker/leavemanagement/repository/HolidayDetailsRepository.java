package com.wavemaker.leavemanagement.repository;

import com.wavemaker.leavemanagement.models.Holidays;

import java.sql.SQLException;
import java.util.List;

public interface HolidayDetailsRepository {
    List<Holidays> getAllHoildays() throws SQLException;
}
