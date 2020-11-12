package com.uber.uberapi.services.drivermatching.filters;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;

import java.util.List;

public interface DriverFilter {
    List<Driver> filter(List<Driver> drivers, Booking booking);
}
