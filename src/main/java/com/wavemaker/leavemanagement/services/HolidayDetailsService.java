package com.wavemaker.leavemanagement.services;

import com.wavemaker.leavemanagement.models.Holidays;

import java.sql.SQLException;
import java.util.List;

public interface HolidayDetailsService {
    List<Holidays> getAllHoildays() throws SQLException;
}
