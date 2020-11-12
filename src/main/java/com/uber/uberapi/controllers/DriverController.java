package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidBookingException;
import com.uber.uberapi.exceptions.InvalidDriverException;
import com.uber.uberapi.models.*;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.DriverRepository;
import com.uber.uberapi.repositories.ReviewRepository;
import com.uber.uberapi.services.BookingService;
import com.uber.uberapi.services.Constants;
import com.uber.uberapi.services.drivermatching.DriverMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/driver")
@RestController
public class DriverController {
    @Autowired
    DriverRepository driverRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DriverMatchingService driverMatchingService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BookingService bookingService;

    @Autowired
    Constants constants;

    public Driver getDriverFromId(Long driverId){
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(!driver.isPresent()){
            throw new InvalidDriverException("No drive with id:"+driverId);
        }
        return driver.get();
    }
    public Booking getDriverBookingFromId(Long bookingId, Driver driver){
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(!optionalBooking.isPresent()){
            throw new InvalidBookingException("No booking with id:"+bookingId);
        }
        Booking booking = optionalBooking.get();
        if(!booking.getDriver().equals(driver)){
            throw new InvalidBookingException("Driver with id:"+driver.getId()+"has no booking with id:"+bookingId);
        }
        return booking;
    }
    @GetMapping("/{driverId}")
    public Driver getDriverDetails(@PathVariable(name = "driverId") Long driverId){
        Driver driver = getDriverFromId(driverId);
        return driver;
    }

    @PatchMapping("/{driverId}")
    public void changeAvailability(@PathVariable(name = "driverId") Long driverId,@RequestBody Boolean available){
        Driver driver = getDriverFromId(driverId);
        driver.setIsAvailable(available);
        driverRepository.save(driver);
    }

    @GetMapping("/{driverId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name="driverId") Long driverId) {
        Driver driver = getDriverFromId(driverId);
        return driver.getBookings();
    }
    @GetMapping("/{driverId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name="driverId") Long driverId,@PathVariable(name = "bookingId") Long bookingId){
        Driver driver =  getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId,driver);
        return booking;
    }
    @PostMapping("/{driverId}/bookings/{bookingId}")
    public void acceptBooking(@PathVariable(name = "driverId") Long driverId,@PathVariable(name = "bookingId") Long bookingId){
        Driver driver= getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId,driver);
        bookingService.acceptBooking(driver,booking);
    }
    @PostMapping("/{driverId}/bookings/{bookingId}/cancel")
    public void cancelBooking(@PathVariable(name = "driverId") Long driverId,@PathVariable(name = "bookingId") Long bookingId){
        Driver driver= getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId,driver);
        bookingService.cancelByDriver(driver,booking);
    }

    @PatchMapping("/{driverId}/bookings/{bookingId}/start")
    public void startRide(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId, @RequestBody OTP otp){
        Driver driver= getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId,driver);
        //confirm otp
        booking.startRide(otp,constants.getRideStartOTPExpiryMin() );
        bookingRepository.save(booking);
        //booking in correct state?
        //booking.setBookingStatus(BookingStatus.IN_RIDE);
    }
    @PatchMapping("/{driverId}/bookings/{bookingId}/end")
    public void endRide(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId){
        Driver driver= getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId,driver);
        booking.endRide();
        driverRepository.save(driver);
        bookingRepository.save(booking);
    }

    @PatchMapping("/{driverId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId,
                         @RequestBody Review data){
        Driver driver= getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId,driver);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByDriver(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }

}
