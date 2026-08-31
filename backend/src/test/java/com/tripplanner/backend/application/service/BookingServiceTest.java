package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.BookingRequestDto;
import com.tripplanner.backend.application.dto.BookingResponseDto;
import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.model.BookingStatus;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.model.Role;
import com.tripplanner.backend.domain.model.User;
import com.tripplanner.backend.domain.exception.InvalidBookingRequestException;
import com.tripplanner.backend.domain.exception.ResourceNotFoundException;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import com.tripplanner.backend.domain.repository.UserDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingDomainRepository bookingRepository;

    @Mock
    private UserDomainRepository userRepository;

    @Mock
    private PackageDomainRepository packageRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void createBooking_shouldPersistAuthenticatedBookingAndCalculateAmount() {
        User currentUser = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Package travelPackage = new Package(5L, "Kerala Premium", "Kerala", "Premium package", 15000);
        BookingRequestDto requestDto = new BookingRequestDto(5L, LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), 2, 1, 1);

        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(currentUser));
        when(packageRepository.findById(5L)).thenReturn(java.util.Optional.of(travelPackage));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(1001L);
            return booking;
        });

        BookingResponseDto responseDto = bookingService.createBooking(requestDto, "john@example.com");

        assertEquals(1001L, responseDto.getId());
        assertEquals("John Doe", responseDto.getCustomerName());
        assertEquals("john@example.com", responseDto.getCustomerEmail());
        assertEquals(5L, responseDto.getPackageId());
        assertEquals("Kerala Premium", responseDto.getPackageName());
        assertEquals("Kerala", responseDto.getDestination());
        assertEquals(new BigDecimal("45000.00"), responseDto.getTotalAmount());
        assertEquals(BookingStatus.PENDING, responseDto.getStatus());
        assertNotNull(responseDto.getCreatedAt());

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking savedBooking = bookingCaptor.getValue();
        assertEquals(10L, savedBooking.getUser().getId());
        assertEquals(5L, savedBooking.getTravelPackage().getId());
        assertEquals("Kerala", savedBooking.getDestination());
        assertEquals(new BigDecimal("45000.00"), savedBooking.getTotalAmount());
    }

    @Test
    public void getBookingById_shouldAllowOwner() {
        User user = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Package travelPackage = new Package(5L, "Kerala Premium", "Kerala", "Premium package", 15000);
        Booking booking = new Booking(1L, user, travelPackage, "Kerala", LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), 2, 1, 1, new BigDecimal("45000.00"), BookingStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        BookingResponseDto responseDto = bookingService.getBookingById(1L, "john@example.com", false);

        assertEquals(1L, responseDto.getId());
        assertEquals("john@example.com", responseDto.getCustomerEmail());
    }

    @Test
    public void getBookingById_shouldRejectNonOwner() {
        User user = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Package travelPackage = new Package(5L, "Kerala Premium", "Kerala", "Premium package", 15000);
        Booking booking = new Booking(1L, user, travelPackage, "Kerala", LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), 2, 1, 1, new BigDecimal("45000.00"), BookingStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.getBookingById(1L, "jane@example.com", false));
    }

    @Test
    public void deleteBooking_shouldRejectNonOwner() {
        User user = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Package travelPackage = new Package(5L, "Kerala Premium", "Kerala", "Premium package", 15000);
        Booking booking = new Booking(1L, user, travelPackage, "Kerala", LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), 2, 1, 1, new BigDecimal("45000.00"), BookingStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));

        assertThrows(AccessDeniedException.class, () -> bookingService.deleteBooking(1L, "jane@example.com", false));
        verify(bookingRepository, never()).deleteById(1L);
    }

    @Test
    public void deleteBooking_shouldCancelOwnBooking() {
        User user = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        Package travelPackage = new Package(5L, "Kerala Premium", "Kerala", "Premium package", 15000);
        Booking booking = new Booking(1L, user, travelPackage, "Kerala", LocalDate.now().plusDays(10), LocalDate.now().plusDays(15), 2, 1, 1, new BigDecimal("45000.00"), BookingStatus.PENDING, LocalDateTime.now(), LocalDateTime.now());
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        bookingService.deleteBooking(1L, "john@example.com", false);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        assertEquals(BookingStatus.CANCELLED, bookingCaptor.getValue().getStatus());
    }

    @Test
    public void getBookingById_shouldThrowWhenMissing() {
        when(bookingRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(1L, "john@example.com", false));
    }

    @Test
    public void createBooking_shouldRejectPastDepartureDate() {
        User currentUser = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(currentUser));

        BookingRequestDto requestDto = new BookingRequestDto(5L, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), 2, 1, 1);

        assertThrows(InvalidBookingRequestException.class, () -> bookingService.createBooking(requestDto, "john@example.com"));
    }

    @Test
    public void createBooking_shouldRejectInvalidTravelerCounts() {
        User currentUser = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(currentUser));

        assertThrows(InvalidBookingRequestException.class, () -> bookingService.createBooking(new BookingRequestDto(5L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 0, 0, 1), "john@example.com"));
        assertThrows(InvalidBookingRequestException.class, () -> bookingService.createBooking(new BookingRequestDto(5L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1, -1, 1), "john@example.com"));
        assertThrows(InvalidBookingRequestException.class, () -> bookingService.createBooking(new BookingRequestDto(5L, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1, 0, 0), "john@example.com"));
    }

    @Test
    public void createBooking_shouldRejectReturnDateBeforeDepartureDate() {
        User currentUser = new User(10L, "John Doe", "john@example.com", "secret", Role.USER, LocalDateTime.now(), LocalDateTime.now());
        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(currentUser));

        BookingRequestDto requestDto = new BookingRequestDto(5L, LocalDate.now().plusDays(5), LocalDate.now().plusDays(2), 2, 1, 1);

        assertThrows(InvalidBookingRequestException.class, () -> bookingService.createBooking(requestDto, "john@example.com"));
    }
}
