package com.uber.uberapi.services.drivermatching.filters;

import com.uber.uberapi.models.Booking;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.ETAService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ETAbasedFilter implements DriverFilter {
    private final ETAService etaService;
    private final Constants constants;

    public ETAbasedFilter(ETAService etaService, Constants constants) {
        this.constants = constants;
        this.etaService = etaService;
    }
    public List<Driver> filter(List<Driver> drivers, Booking booking) {
        if(!constants.getETAbasedFilterIsOn()) return drivers;
        ExactLocation pickup = booking.getPickupLocation();
        return drivers.stream().filter(driver -> {
          return etaService.getETAMinutes(driver.getLastKnownLocation(),pickup)<=constants.getMaxDriverETAMinutes();
        }).collect(Collectors.toList());
    }
}
