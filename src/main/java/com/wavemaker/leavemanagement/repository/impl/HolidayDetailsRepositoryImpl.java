package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.exception.LeaveRepositoryException;
import com.wavemaker.leavemanagement.models.Holidays;
import com.wavemaker.leavemanagement.models.dto.LeaveEmployeeV0;
import com.wavemaker.leavemanagement.repository.HolidayDetailsRepository;
import com.wavemaker.leavemanagement.utils.DBConnections;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HolidayDetailsRepositoryImpl implements HolidayDetailsRepository {

    private final Connection connection = DBConnections.getConnection();

    private static final String GET_ALL_HOLIDAYS = "select * from holidays;";

    public HolidayDetailsRepositoryImpl() throws SQLException {
    }

    @Override
    public List<Holidays> getAllHoildays() throws SQLException {

        List<Holidays> respList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_HOLIDAYS)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String HolidayName = resultSet.getString(1);
                    Date sqlDate = resultSet.getDate(2);
                    String holidayType = resultSet.getString(3);

                    LocalDate localFromDate = sqlDate.toLocalDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    String HolidayDate = localFromDate.format(formatter);

                    Holidays holidaysObj = new Holidays(HolidayName , HolidayDate , holidayType);
                    respList.add(holidaysObj);
                }


            } catch (Exception e) {
                throw new LeaveRepositoryException("Could not insert leave", e);
            }
        }

        return respList;
    }
}
