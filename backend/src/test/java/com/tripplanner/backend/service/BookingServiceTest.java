package com.tripplanner.backend.service;

import com.tripplanner.backend.dto.BookingRequestDto;
import com.tripplanner.backend.dto.BookingResponseDto;
import com.tripplanner.backend.model.Booking;
import com.tripplanner.backend.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

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
}
