package com.uber.uberapi.services;

import com.uber.uberapi.models.ExactLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ETAService {
    @Autowired
    Constants constants;
    public Integer getETAMinutes(ExactLocation lastKnownLocation, ExactLocation pickup) {
        return (int)(60 * lastKnownLocation.distanceInKm(pickup) / constants.getDefaultDriverSpeedInKMPH());
    }
}
