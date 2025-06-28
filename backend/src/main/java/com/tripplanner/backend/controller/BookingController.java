package com.tripplanner.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripplanner.backend.model.Booking;
import com.tripplanner.backend.model.BookingRequest;
import com.tripplanner.backend.repository.BookingRepository;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping
    public Map<String, String> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = new Booking(bookingRequest.getName(), bookingRequest.getEmail(), bookingRequest.getPackageName());
        bookingRepository.save(booking);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Booking saved for " + booking.getName() + " (" + booking.getEmail() + ") for package: " + booking.getPackageName());
        return response;
    }

    @GetMapping
    public Iterable<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
