package com.tripplanner.backend.domain.repository;

import com.tripplanner.backend.domain.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookingDomainRepository {
    Booking save(Booking booking);
    Page<Booking> findAll(Pageable pageable);
    Optional<Booking> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
    Page<Booking> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
