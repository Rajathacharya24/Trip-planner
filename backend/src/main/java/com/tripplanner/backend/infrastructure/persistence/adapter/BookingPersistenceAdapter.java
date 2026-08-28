package com.tripplanner.backend.infrastructure.persistence.adapter;

import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.infrastructure.persistence.entity.BookingEntity;
import com.tripplanner.backend.infrastructure.persistence.repository.SpringDataBookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookingPersistenceAdapter implements BookingDomainRepository {

    private final SpringDataBookingRepository bookingRepository;

    public BookingPersistenceAdapter(SpringDataBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking save(Booking booking) {
        BookingEntity entity = toEntity(booking);
        BookingEntity savedEntity = bookingRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Page<Booking> findByEmailContainingIgnoreCase(String email, Pageable pageable) {
        return bookingRepository.findByEmailContainingIgnoreCase(email, pageable).map(this::toDomain);
    }

    private BookingEntity toEntity(Booking booking) {
        return new BookingEntity(
                booking.getId(),
                booking.getName(),
                booking.getEmail(),
                booking.getPackageName(),
                booking.getStatus()
        );
    }

    private Booking toDomain(BookingEntity entity) {
        return new Booking(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPackageName(),
                entity.getStatus()
        );
    }
}
