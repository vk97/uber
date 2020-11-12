package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum BookingStatus {
    ASSIGNING_DRIVER("The passenger has requested, driver not assigned yet"),
    SCHEDULED("Scheduled for sometime in future"),
    CAB_ARRIVED("Cab has arrived to passengers location"),
    COMPLETED("Ride has been completed"),
    CANCELLED("Cancelled for some reason"),
    IN_RIDE("Ride is currently in progress"),
    REACHING_PICKUP_LOCATION("Driver on his way");

    private final String description;
    BookingStatus(String description){
        this.description=description;
    }
}
