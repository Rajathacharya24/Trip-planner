package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.BookingRequestDto;
import com.tripplanner.backend.application.dto.BookingResponseDto;
import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.exception.ResourceNotFoundException;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingDomainRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void createBooking_shouldReturnBookingResponseDto() {
        BookingRequestDto requestDto = new BookingRequestDto("John Doe", "john@example.com", "Luxury Package");
        Booking savedBooking = new Booking(1L, "John Doe", "john@example.com", "Luxury Package");

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingResponseDto responseDto = bookingService.createBooking(requestDto);

        assertEquals("John Doe", responseDto.getName());
        assertEquals("john@example.com", responseDto.getEmail());
        assertEquals("Luxury Package", responseDto.getPackageName());
        assertEquals(1L, responseDto.getId());
    }

    @Test
    public void getBookingById_shouldAllowOwner() {
        Booking booking = new Booking(1L, "John Doe", "john@example.com", "Luxury Package");
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        BookingResponseDto responseDto = bookingService.getBookingById(1L, "john@example.com", false);

        assertEquals(1L, responseDto.getId());
        assertEquals("john@example.com", responseDto.getEmail());
    }

    @Test
    public void getBookingById_shouldRejectNonOwner() {
        Booking booking = new Booking(1L, "John Doe", "john@example.com", "Luxury Package");
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.getBookingById(1L, "jane@example.com", false));
    }

    @Test
    public void deleteBooking_shouldRejectNonOwner() {
        Booking booking = new Booking(1L, "John Doe", "john@example.com", "Luxury Package");
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.deleteBooking(1L, "jane@example.com", false));
        verify(bookingRepository, never()).deleteById(1L);
    }

    @Test
    public void getBookingById_shouldThrowWhenMissing() {
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(1L, "john@example.com", false));
    }
}
