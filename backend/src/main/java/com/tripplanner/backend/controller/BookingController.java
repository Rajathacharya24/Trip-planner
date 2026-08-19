package com.tripplanner.backend.controller;

import com.tripplanner.backend.dto.BookingRequestDto;
import com.tripplanner.backend.dto.BookingResponseDto;
import com.tripplanner.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Controller", description = "Endpoints for managing bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new booking")
    public BookingResponseDto createBooking(@Valid @RequestBody BookingRequestDto requestDto) {
        log.info("Received request to create booking");
        return bookingService.createBooking(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all bookings with pagination")
    public Page<BookingResponseDto> getAllBookings(Pageable pageable) {
        log.info("Received request to get all bookings");
        return bookingService.getAllBookings(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public BookingResponseDto getBookingById(@PathVariable Long id) {
        log.info("Received request to get booking by ID: {}", id);
        return bookingService.getBookingById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing booking")
    public BookingResponseDto updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequestDto requestDto) {
        log.info("Received request to update booking with ID: {}", id);
        return bookingService.updateBooking(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a booking")
    public void deleteBooking(@PathVariable Long id) {
        log.info("Received request to delete booking with ID: {}", id);
        bookingService.deleteBooking(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search bookings by email")
    public Page<BookingResponseDto> searchBookings(@RequestParam String email, Pageable pageable) {
        log.info("Received request to search bookings by email: {}", email);
        return bookingService.searchBookingsByEmail(email, pageable);
    }
}
