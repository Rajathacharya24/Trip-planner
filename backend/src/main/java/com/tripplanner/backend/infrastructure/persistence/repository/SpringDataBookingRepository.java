package com.tripplanner.backend.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tripplanner.backend.infrastructure.persistence.entity.BookingEntity;

@Repository
public interface SpringDataBookingRepository extends JpaRepository<BookingEntity, Long> {
    Page<BookingEntity> findByUser_Id(Long userId, Pageable pageable);
    Page<BookingEntity> findByUser_EmailContainingIgnoreCase(String email, Pageable pageable);
}
