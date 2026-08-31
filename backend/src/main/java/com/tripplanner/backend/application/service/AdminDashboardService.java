package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.AdminDashboardResponse;
import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import com.tripplanner.backend.domain.repository.UserDomainRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.math.BigDecimal;

@Service
public class AdminDashboardService {

    private final UserDomainRepository userRepository;
    private final PackageDomainRepository packageRepository;
    private final BookingDomainRepository bookingRepository;

    public AdminDashboardService(UserDomainRepository userRepository,
                                 PackageDomainRepository packageRepository,
                                 BookingDomainRepository bookingRepository) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.bookingRepository = bookingRepository;
    }

    public AdminDashboardResponse getDashboardStats() {
        long totalUsers = userRepository.count();
        List<Booking> bookings = bookingRepository.findAll(Pageable.unpaged()).getContent();

        long totalRevenue = 0;
        for (Booking booking : bookings) {
            if (booking.getStatus() != null && (booking.getStatus().name().equalsIgnoreCase("CONFIRMED") || booking.getStatus().name().equalsIgnoreCase("COMPLETED"))) {
                BigDecimal amount = booking.getTotalAmount();
                if (amount != null) {
                    totalRevenue += amount.longValue();
                }
            }
        }

        return new AdminDashboardResponse(
                totalUsers,
                packageRepository.findAll(Pageable.unpaged()).getContent().size(),
                bookings.size(),
                totalRevenue
        );
    }
}