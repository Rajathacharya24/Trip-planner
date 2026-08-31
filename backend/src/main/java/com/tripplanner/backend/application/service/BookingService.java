package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.BookingRequestDto;
import com.tripplanner.backend.application.dto.BookingResponseDto;
import com.tripplanner.backend.domain.exception.InvalidBookingRequestException;
import com.tripplanner.backend.domain.exception.ResourceNotFoundException;
import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.model.BookingStatus;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.model.User;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import com.tripplanner.backend.domain.repository.UserDomainRepository;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingDomainRepository bookingRepository;
    private final UserDomainRepository userRepository;
    private final PackageDomainRepository packageRepository;

    public BookingService(BookingDomainRepository bookingRepository,
                          UserDomainRepository userRepository,
                          PackageDomainRepository packageRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto requestDto, String currentUserEmail) {
        validateBookingRequest(requestDto);

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + currentUserEmail));

        Package travelPackage = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + requestDto.getPackageId()));

        validateDates(requestDto.getDepartureDate(), requestDto.getReturnDate());

        BigDecimal totalAmount = calculateTotalAmount(travelPackage.getPrice(), requestDto.getAdults(), requestDto.getChildren(), requestDto.getRooms());

        Booking booking = new Booking();
        booking.setUser(currentUser);
        booking.setTravelPackage(travelPackage);
        booking.setDestination(travelPackage.getDestination());
        booking.setDepartureDate(requestDto.getDepartureDate());
        booking.setReturnDate(requestDto.getReturnDate());
        booking.setAdults(requestDto.getAdults());
        booking.setChildren(requestDto.getChildren());
        booking.setRooms(requestDto.getRooms());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with ID: {}", savedBooking.getId());
        return mapToDto(savedBooking);
    }

    public Page<BookingResponseDto> getAllBookings(Pageable pageable) {
        log.debug("Fetching all bookings with pagination: {}", pageable);
        return bookingRepository.findAll(pageable).map(this::mapToDto);
    }

    public Page<BookingResponseDto> getMyBookings(String currentUserEmail, Pageable pageable) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + currentUserEmail));
        return bookingRepository.findByUserId(currentUser.getId(), pageable).map(this::mapToDto);
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

    public BookingResponseDto getBookingById(Long id, String currentUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        verifyOwnershipOrAdmin(booking, currentUserEmail, isAdmin);
        return mapToDto(booking);
    }

    @Transactional
    public BookingResponseDto updateBooking(Long id, BookingRequestDto requestDto, String currentUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        verifyOwnershipOrAdmin(booking, currentUserEmail, isAdmin);

        validateBookingRequest(requestDto);

        Package travelPackage = packageRepository.findById(requestDto.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + requestDto.getPackageId()));

        validateDates(requestDto.getDepartureDate(), requestDto.getReturnDate());

        booking.setTravelPackage(travelPackage);
        booking.setDestination(travelPackage.getDestination());
        booking.setDepartureDate(requestDto.getDepartureDate());
        booking.setReturnDate(requestDto.getReturnDate());
        booking.setAdults(requestDto.getAdults());
        booking.setChildren(requestDto.getChildren());
        booking.setRooms(requestDto.getRooms());
        booking.setTotalAmount(calculateTotalAmount(travelPackage.getPrice(), requestDto.getAdults(), requestDto.getChildren(), requestDto.getRooms()));

        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDto(updatedBooking);
    }
 
     @Transactional
     public BookingResponseDto updateBookingStatus(Long id, BookingStatus status) {
         log.info("Updating status for booking ID: {} to {}", id, status);
         Booking booking = bookingRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
         booking.setStatus(status);
         booking.setUpdatedAt(LocalDateTime.now());
         Booking updatedBooking = bookingRepository.save(booking);
         return mapToDto(updatedBooking);
     }

    @Transactional
    public void deleteBooking(Long id, String currentUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        verifyOwnershipOrAdmin(booking, currentUserEmail, isAdmin);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public Page<BookingResponseDto> searchBookingsByEmail(String email, Pageable pageable) {
        log.debug("Searching bookings by email containing: {}", email);
        return bookingRepository.findByEmailContainingIgnoreCase(email, pageable).map(this::mapToDto);
    }

    private BookingResponseDto mapToDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getUser() != null ? booking.getUser().getName() : null,
                booking.getUser() != null ? booking.getUser().getEmail() : null,
                booking.getTravelPackage() != null ? booking.getTravelPackage().getId() : null,
                booking.getTravelPackage() != null ? booking.getTravelPackage().getName() : null,
                booking.getDestination(),
                booking.getDepartureDate(),
                booking.getReturnDate(),
                booking.getAdults(),
                booking.getChildren(),
                booking.getRooms(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }

    private void verifyOwnershipOrAdmin(Booking booking, String currentUserEmail, boolean isAdmin) {
        if (isAdmin) {
            return;
        }

        if (currentUserEmail == null || booking.getUser() == null || !booking.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new AccessDeniedException("Access denied to this booking");
        }
    }

    private void validateBookingRequest(BookingRequestDto requestDto) {
        if (requestDto.getPackageId() == null) {
            throw new InvalidBookingRequestException("Package ID is required");
        }
        if (requestDto.getDepartureDate() == null) {
            throw new InvalidBookingRequestException("Departure date is required");
        }
        if (requestDto.getReturnDate() == null) {
            throw new InvalidBookingRequestException("Return date is required");
        }
        if (requestDto.getAdults() == null || requestDto.getAdults() < 1) {
            throw new InvalidBookingRequestException("Adults must be greater than 0");
        }
        if (requestDto.getChildren() == null || requestDto.getChildren() < 0) {
            throw new InvalidBookingRequestException("Children cannot be negative");
        }
        if (requestDto.getRooms() == null || requestDto.getRooms() < 1) {
            throw new InvalidBookingRequestException("Rooms must be at least 1");
        }
    }

    private void validateDates(LocalDate departureDate, LocalDate returnDate) {
        LocalDate today = LocalDate.now();
        if (departureDate.isBefore(today)) {
            throw new InvalidBookingRequestException("Departure date cannot be in the past");
        }
        if (!returnDate.isAfter(departureDate)) {
            throw new InvalidBookingRequestException("Return date must be after departure date");
        }
    }

    private BigDecimal calculateTotalAmount(int packagePrice, Integer adults, Integer children, Integer rooms) {
        BigDecimal basePrice = BigDecimal.valueOf(packagePrice);
        BigDecimal adultCharge = basePrice.multiply(BigDecimal.valueOf(adults));
        BigDecimal childCharge = basePrice.multiply(BigDecimal.valueOf(children)).multiply(BigDecimal.valueOf(0.5));
        BigDecimal roomCharge = basePrice.multiply(BigDecimal.valueOf(rooms)).multiply(BigDecimal.valueOf(0.5));
        return adultCharge.add(childCharge).add(roomCharge);
    }
}
