package com.uber.uberapi.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "driver")
public class Driver extends Auditable{
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
    private Gender gender;
    private String name;

    @OneToOne(mappedBy = "driver")
    private Car car;

    private String licenseDetails;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Enumerated(EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings = new ArrayList<>();

    private Boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation home;

    @OneToOne
    private ExactLocation lastKnownLocation;
}
