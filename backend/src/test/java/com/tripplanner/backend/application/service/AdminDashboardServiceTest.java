package com.tripplanner.backend.application.service;

import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import com.tripplanner.backend.domain.repository.UserDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminDashboardServiceTest {

    @Mock
    private UserDomainRepository userRepository;

    @Mock
    private PackageDomainRepository packageRepository;

    @Mock
    private BookingDomainRepository bookingRepository;

    @InjectMocks
    private AdminDashboardService adminDashboardService;

    @Test
    public void getDashboardStats_shouldAggregateCountsAndRevenue() {
        when(userRepository.count()).thenReturn(5L);
        when(packageRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(
                new Package(1L, "Luxury Package", "Premium", 35000),
                new Package(2L, "Ordinary Package", "Standard", 15000)
        )));
        when(bookingRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(
                new Booking(1L, "A", "a@example.com", "Luxury Package", "CONFIRMED"),
                new Booking(2L, "B", "b@example.com", "Ordinary Package", "PENDING")
        )));

        var stats = adminDashboardService.getDashboardStats();

        assertEquals(5L, stats.getTotalUsers());
        assertEquals(2L, stats.getTotalPackages());
        assertEquals(2L, stats.getTotalBookings());
        assertEquals(35000L, stats.getTotalRevenue());
    }
}