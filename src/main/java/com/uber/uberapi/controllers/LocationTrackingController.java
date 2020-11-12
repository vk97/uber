package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidDriverException;
import com.uber.uberapi.exceptions.InvalidPassengerException;
import com.uber.uberapi.models.Driver;
import com.uber.uberapi.models.ExactLocation;
import com.uber.uberapi.models.Passenger;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.services.locationtracking.LocationTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/location")
public class LocationTrackingController {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    LocationTrackingService locationTrackingService;

    public Driver getDriverFromId(Long driverId){
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(!driver.isPresent()){
            throw new InvalidDriverException("No drive with id:"+driverId);
        }
        return driver.get();
    }
    public Passenger getPassengerFromId(Long passengerId){
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(!passenger.isPresent()){
            throw new InvalidPassengerException("No drive with id:"+passengerId);
        }
        return passenger.get();
    }
    @PutMapping("/driver/{driverId}")
    public void updateDriverLocation(@PathVariable Long driverId,ExactLocation location){
        Driver driver = getDriverFromId(driverId);
        ExactLocation latestLocation = ExactLocation.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
        locationTrackingService.updateDriverLocation(driver,latestLocation);
    }
    @PutMapping("/passenger/{passengerId}")
    public void updatePassengerLocation(@PathVariable Long passengerId,ExactLocation location){
        Passenger passenger = getPassengerFromId(passengerId);
        ExactLocation lastKnownLocation = ExactLocation.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
        passenger.setLastKnownLocation(lastKnownLocation);
        passengerRepository.save(passenger);
    }
}
