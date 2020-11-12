package com.uber.uberapi.services;

import com.uber.uberapi.exceptions.InvalidActionForBookingStateException;
import com.uber.uberapi.models.*;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.services.drivermatching.DriverMatchingService;
import com.uber.uberapi.services.messagequeue.MessageQueue;
import com.uber.uberapi.services.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    DriverMatchingService driverMatchingService;
    @Autowired
    OTPService otpService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    SchedulingService schedulingService;

    @Autowired
    MessageQueue messageQueue;
    @Autowired
    DriverRepository driverRepository;
    @Autowired
    Constants constants;
    @Autowired
    NotificationService notificationService;
    public void createBooking(Booking booking) {
        if(booking.getStartTime().after(new Date())){
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            messageQueue.sendMessage(constants.getSchedulingTopicName(), new SchedulingService.Message(booking));
        }else{
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOtp());
            messageQueue.sendMessage(constants.getDriverMatchingTopicName(),new DriverMatchingService.Message(booking));
        }
        bookingRepository.save(booking);
        passengerRepository.save(booking.getPassenger());
    }


    public void cancelByDriver(Driver driver, Booking booking) {
        booking.setDriver(null);
        driver.setActiveBooking(null);
        driver.getAcceptableBookings().remove(booking);
        notificationService.notify(booking.getPassenger().getPhoneNumber(),"Sorry driver had to cancel. Reassigning driver");
        notificationService.notify(booking.getDriver().getPhoneNumber(),"Cancelled the ride");
        retryBooking(booking);
        bookingRepository.save(booking);
    }


    public void acceptBooking(Driver driver, Booking booking) {
        if(!booking.needsDriver()){
            return;
        }
        if(!driver.canAcceptBooking(constants.getMaxWaitTimeForPreviousRide())){
            notificationService.notify(driver.getPhoneNumber(),"Cant accept while in a ride");
        }
        booking.setDriver(driver);
        driver.setActiveBooking(booking);
        booking.getNotifiedDrivers().clear();
        driver.getAcceptableBookings().clear();
        
        notificationService.notify(booking.getPassenger().getPhoneNumber(),"Driver is arriving");
        notificationService.notify(driver.getPhoneNumber(),"booking Accepted");
        bookingRepository.save(booking);
        driverRepository.save(driver);
    }


    public void cancelByPassenger(Passenger passenger, Booking booking) {
        try{
            booking.cancel();
        }
        catch(InvalidActionForBookingStateException inner){
            notificationService.notify(booking.getPassenger().getPhoneNumber()
                    ,"Cant cancel the booking now. If ride in progress, request the driver to end the ride");
            throw inner;
        }

        bookingRepository.save(booking);
    }

    public void updateRoute(Booking booking, List<ExactLocation> route) {
        if(!booking.canChangeRoute()){
            throw new InvalidActionForBookingStateException("Route cant be updated now.");
        }
        booking.setRoute(route);
        bookingRepository.save(booking);
        notificationService.notify(booking.getDriver().getPhoneNumber(),"Route has been updated");
    }

    public void retryBooking(Booking booking) {
        createBooking(booking);
    }
}
