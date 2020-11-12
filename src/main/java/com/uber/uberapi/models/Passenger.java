package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passenger")
public class Passenger extends Auditable  {
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings = new ArrayList<>();

    private String phoneNumber;

    @Temporal(value=TemporalType.DATE)
    private Date dob;

    @OneToOne
    private ExactLocation home;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private ExactLocation work;
}
