package com.uber.uberapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="passenger")
public class Passenger extends Auditable  {
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings = new ArrayList<>();

    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @OneToOne
    private ExactLocation home;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private ExactLocation work;
}
