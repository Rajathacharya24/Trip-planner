package com.tripplanner.backend.infrastructure.web.controller;

import com.tripplanner.backend.application.dto.BookingRequestDto;
import com.tripplanner.backend.application.dto.BookingResponseDto;
import com.tripplanner.backend.application.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking Controller", description = "Endpoints for managing bookings")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new booking")
    public BookingResponseDto createBooking(@Valid @RequestBody BookingRequestDto requestDto) {
        log.info("Received request to create booking");
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        requestDto.setEmail(currentUserEmail);
        return bookingService.createBooking(requestDto);
    }

    @GetMapping("/my")
    @Operation(summary = "Get bookings for the authenticated user")
    public Page<BookingResponseDto> getMyBookings(Pageable pageable) {
        log.info("Received request to get my bookings");
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return bookingService.searchBookingsByEmail(currentUserEmail, pageable);
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
        BookingResponseDto booking = bookingService.getBookingById(id);
        
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !booking.getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied to this booking");
        }
        return booking;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing booking")
    public BookingResponseDto updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequestDto requestDto) {
        log.info("Received request to update booking with ID: {}", id);
        BookingResponseDto booking = bookingService.getBookingById(id);
        
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !booking.getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied to this booking");
        }
        
        if (!isAdmin) {
            requestDto.setEmail(currentUserEmail);
        }
        return bookingService.updateBooking(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a booking")
    public void deleteBooking(@PathVariable Long id) {
        log.info("Received request to delete booking with ID: {}", id);
        BookingResponseDto booking = bookingService.getBookingById(id);
        
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        if (!isAdmin && !booking.getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new org.springframework.security.access.AccessDeniedException("Access denied to this booking");
        }
        bookingService.deleteBooking(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search bookings by email")
    public Page<BookingResponseDto> searchBookings(@RequestParam String email, Pageable pageable) {
        log.info("Received request to search bookings by email: {}", email);
        return bookingService.searchBookingsByEmail(email, pageable);
    }
}
