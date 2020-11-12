package com.uber.uberapi.models;

import com.uber.uberapi.exceptions.InvalidActionForBookingStateException;
import com.uber.uberapi.exceptions.InvalidOTPException;
import com.uber.uberapi.services.Constants;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "booking", indexes = {
        @Index(columnList = "passenger_id"),
        @Index(columnList = "driver_id")
})
@Getter
@Setter
@Builder
public class Booking extends Auditable{
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Driver> notifiedDrivers = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private BookingType bookingtype;

    @OneToOne
    private Review reviewByPassenger;

    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = @Index(columnList = "booking_id")
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> route = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_completed_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = @Index(columnList = "booking_id")
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> completedRoute = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedCompletionTime;

    @OneToOne
    private OTP rideStartOtp;

    private Long totalDistanceMeters;

    public void startRide(OTP otp, Integer rideStartOTPExpiryMinutes) {
        if(!bookingStatus.equals(BookingStatus.CAB_ARRIVED)){
            throw new InvalidActionForBookingStateException("Cannot start ride with the booking state:" +bookingStatus);
        }
        rideStartOtp.validateEnteredOTP(otp,rideStartOTPExpiryMinutes);//check for validating with an expiry?
        bookingStatus = BookingStatus.IN_RIDE;
    }

    public void endRide() {
        if(!bookingStatus.equals(BookingStatus.IN_RIDE)){
            throw new InvalidActionForBookingStateException("Cannot end ride with the booking state:" +bookingStatus);
        }
        driver.setActiveBooking(null);
        bookingStatus = BookingStatus.COMPLETED;
    }

    public boolean canChangeRoute() {
        return bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
                ||bookingStatus.equals(BookingStatus.CAB_ARRIVED)
                ||bookingStatus.equals(BookingStatus.IN_RIDE)
                ||bookingStatus.equals(BookingStatus.SCHEDULED)
                ||bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION);
    }

    public boolean needsDriver() {
        return bookingStatus.equals(bookingStatus.ASSIGNING_DRIVER);
    }

    public ExactLocation getPickupLocation() {
        return route.get(0);
    }

    public void cancel() {
        if(!(bookingStatus.equals(BookingStatus.REACHING_PICKUP_LOCATION)
                ||bookingStatus.equals(BookingStatus.ASSIGNING_DRIVER)
                ||bookingStatus.equals(BookingStatus.SCHEDULED)
                ||bookingStatus.equals(BookingStatus.CAB_ARRIVED))){
            throw new InvalidActionForBookingStateException("Cant cancel now. If ride is in progress please ask the driver to end the ride");
        }
        bookingStatus= BookingStatus.CANCELLED;
        driver=null;
        notifiedDrivers.clear();
    }
}

