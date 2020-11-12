package com.uber.uberapi.controllers;

import com.uber.uberapi.exceptions.InvalidBookingException;
import com.uber.uberapi.exceptions.InvalidPassengerException;
import com.uber.uberapi.models.*;
import com.uber.uberapi.repositories.BookingRepository;
import com.uber.uberapi.repositories.PassengerRepository;
import com.uber.uberapi.repositories.ReviewRepository;
import com.uber.uberapi.services.BookingService;
import com.uber.uberapi.services.drivermatching.DriverMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/passenger")
@RestController
public class PassengerController {

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DriverMatchingService driverMatchingService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BookingService bookingService;

    public Passenger getPassengerFromId(Long passengerId){
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(!passenger.isPresent()){
            throw new InvalidPassengerException("No drive with id:"+passengerId);
        }
        return passenger.get();
    }
    public Booking getPassengerBookingFromId(Long bookingId, Passenger passenger){
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(!optionalBooking.isPresent()){
            throw new InvalidBookingException("No booking with id:"+bookingId);
        }
        Booking booking = optionalBooking.get();
        if(!booking.getPassenger().equals(passenger)){
            throw new InvalidBookingException("Passenger with id:"+passenger.getId()+"has no booking with id:"+bookingId);
        }
        return booking;
    }
    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@PathVariable(name = "passengerId") Long passengerId){
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger;
    }

    @GetMapping("/{passengerId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name="passengerId") Long passengerId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    @GetMapping("/{passengerId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name="passengerId") Long passengerId,@PathVariable(name = "bookingId") Long bookingId){
        Passenger passenger =  getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId,passenger);
        return booking;
    }
    @PostMapping("/{passengerId}/bookings")
    public void requestBooking(@PathVariable(name = "passengerId") Long passengerId,@RequestBody Booking data){
        Passenger passenger= getPassengerFromId(passengerId);
        List<ExactLocation> route = new ArrayList<>();
        data.getRoute().forEach(exactLocation -> {
            route.add(ExactLocation.builder()
                    .latitude(exactLocation.getLatitude())
                    .longitude(exactLocation.getLongitude())
                    .build());
        });
        Booking booking = Booking.builder()
                .rideStartOtp(OTP.make(passenger.getPhoneNumber()))
                .route(route)
                .passenger(passenger)
                .bookingtype(data.getBookingtype())
                .scheduledTime(data.getScheduledTime())
                .build();
        bookingService.createBooking(booking);
    }
    @PatchMapping("/{passengerId}/bookings/{bookingId}")
    public void updateRoute(@PathVariable(name="passengerId") Long passengerId,@PathVariable(name = "bookingId") Long bookingId,
                            @RequestBody Booking data){
        Passenger passenger =  getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId,passenger);
        List<ExactLocation> route = new ArrayList<>();
        // visited locations cant be changed
        route.addAll(booking.getCompletedRoute());
        data.getRoute().forEach(exactLocation -> {
            route.add(ExactLocation.builder()
                    .latitude(exactLocation.getLatitude())
                    .longitude(exactLocation.getLongitude())
                    .build());
        });
        bookingService.updateRoute(booking,route);
    }
    @DeleteMapping("/{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "bookingId") Long bookingId){
        Passenger passenger= getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId,passenger);
        bookingService.cancelByPassenger(passenger,booking);
    }
    @PostMapping("/{passengerId}/bookings/{bookingId}")
    public void retryBooking(@PathVariable(name = "passengerId") Long passengerId,
                             @PathVariable(name = "bookingId") Long bookingId){
        Passenger passenger= getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId,passenger);
        bookingService.retryBooking(booking);
    }
    @PatchMapping("/{passengerId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "passengerId") Long passengerId, @PathVariable(name = "bookingId") Long bookingId,
                         @RequestBody Review data){
        Passenger passenger= getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId,passenger);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByPassenger(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }
}
