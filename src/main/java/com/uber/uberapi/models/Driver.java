package com.uber.uberapi.models;


import com.uber.uberapi.exceptions.UnapprovedeDriverException;
import com.uber.uberapi.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "driver")
public class Driver extends Auditable{
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;
    private Gender gender;
    private String name;
    private String phoneNumber;

    @OneToOne(mappedBy = "driver")
    private Car car;

    private String licenseDetails;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Enumerated(EnumType.STRING)
    private DriverApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings = new ArrayList<>();

    @ManyToMany(mappedBy = "notifiedDrivers",cascade = CascadeType.PERSIST)
    private Set<Booking> acceptableBookings = new HashSet<>();

    private Boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation home;

    @OneToOne
    private Booking activeBooking = null;

    @OneToOne
    private ExactLocation lastKnownLocation;

    public void setAvailable(Boolean available) {

        if(available && !approvalStatus.equals(DriverApprovalStatus.APPROVED)){
            throw new UnapprovedeDriverException("Driver not approved yet" + getId());
        }
        isAvailable = available;
    }

    public boolean canAcceptBooking(int maxWaitTimeForPreviousRide) {
        if(isAvailable && activeBooking==null){
            return true;
        }
        return activeBooking.getExpectedCompletionTime().before(DateUtils.addMinutes(new Date(),maxWaitTimeForPreviousRide));
    }

}
