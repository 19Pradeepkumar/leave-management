package com.wavemaker.leavemanagement.services.impl;

import com.wavemaker.leavemanagement.models.Holidays;
import com.wavemaker.leavemanagement.repository.HolidayDetailsRepository;
import com.wavemaker.leavemanagement.repository.impl.HolidayDetailsRepositoryImpl;
import com.wavemaker.leavemanagement.services.HolidayDetailsService;

import java.sql.SQLException;
import java.util.List;

public class HolidayDetailsServiceImpl implements HolidayDetailsService {
    public HolidayDetailsRepository holidayDetailsRepository = new HolidayDetailsRepositoryImpl();

    public HolidayDetailsServiceImpl() throws SQLException {
    }

    @Override
    public List<Holidays> getAllHoildays() throws SQLException {
        return holidayDetailsRepository.getAllHoildays();
    }
}
