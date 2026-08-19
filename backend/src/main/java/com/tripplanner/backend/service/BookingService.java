package com.tripplanner.backend.service;

import com.tripplanner.backend.dto.BookingRequestDto;
import com.tripplanner.backend.dto.BookingResponseDto;
import com.tripplanner.backend.exception.ResourceNotFoundException;
import com.tripplanner.backend.model.Booking;
import com.tripplanner.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto requestDto) {
        log.info("Creating new booking for email: {}", requestDto.getEmail());
        Booking booking = Booking.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .packageName(requestDto.getPackageName())
                .build();
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with ID: {}", savedBooking.getId());
        return mapToDto(savedBooking);
    }

    public Page<BookingResponseDto> getAllBookings(Pageable pageable) {
        log.debug("Fetching all bookings with pagination: {}", pageable);
        return bookingRepository.findAll(pageable).map(this::mapToDto);
    }

    public BookingResponseDto getBookingById(Long id) {
        log.debug("Fetching booking by ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Booking not found with ID: {}", id);
                    return new ResourceNotFoundException("Booking not found with id: " + id);
                });
        return mapToDto(booking);
    }

    @Transactional
    public BookingResponseDto updateBooking(Long id, BookingRequestDto requestDto) {
        log.info("Updating booking with ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setName(requestDto.getName());
        booking.setEmail(requestDto.getEmail());
        booking.setPackageName(requestDto.getPackageName());
        
        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking updated successfully for ID: {}", id);
        return mapToDto(updatedBooking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        log.info("Deleting booking with ID: {}", id);
        if (!bookingRepository.existsById(id)) {
            log.warn("Cannot delete. Booking not found with ID: {}", id);
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
        log.info("Booking deleted successfully for ID: {}", id);
    }

    public Page<BookingResponseDto> searchBookingsByEmail(String email, Pageable pageable) {
        log.debug("Searching bookings by email containing: {}", email);
        return bookingRepository.findByEmailContainingIgnoreCase(email, pageable).map(this::mapToDto);
    }

    private BookingResponseDto mapToDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .name(booking.getName())
                .email(booking.getEmail())
                .packageName(booking.getPackageName())
                .build();
    }
}
