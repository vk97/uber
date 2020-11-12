package com.uber.uberapi.services.drivermatching.filters;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.Gender;
import com.uber.uberapi.services.Constants;

import java.util.List;
import java.util.stream.Collectors;

public class GenderBasedFilter implements DriverFilter {
    Constants constants;
    public GenderBasedFilter(Constants constants) {
        this.constants=constants;
    }

    @Override
    public List<Driver> filter(List<Driver> drivers, Booking booking) {
        if(!constants.getGenderBasedFilterIsOn()) return drivers;
        return drivers.stream().filter(driver -> {
            Gender driverGender = driver.getGender();
            Gender passengerGender = booking.getPassenger().getGender();
            return !driverGender.equals(Gender.MALE) || passengerGender.equals(Gender.MALE);
        }).collect(Collectors.toList());
    }
}
