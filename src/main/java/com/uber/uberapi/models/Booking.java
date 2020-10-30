package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "booking")
@Getter
@Setter
public class Booking extends Auditable{
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private BookingType bookingtype;

    @OneToOne
    private Review reviewByUser;

    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @OneToMany
    private List<ExactLocation> route = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToOne
    private OTP rideStartOtp;

    private Long totalDistanceMeters;
}
