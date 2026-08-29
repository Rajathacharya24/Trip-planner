package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.AdminDashboardResponse;
import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import com.tripplanner.backend.domain.repository.UserDomainRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Package> packages = packageRepository.findAll(Pageable.unpaged()).getContent();
        List<Booking> bookings = bookingRepository.findAll(Pageable.unpaged()).getContent();

        Map<String, Integer> priceByName = new HashMap<>();
        for (Package travelPackage : packages) {
            priceByName.put(travelPackage.getName().toLowerCase(), travelPackage.getPrice());
        }

        long totalRevenue = 0;
        for (Booking booking : bookings) {
            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus()) || "COMPLETED".equalsIgnoreCase(booking.getStatus())) {
                Integer price = priceByName.get(booking.getPackageName().toLowerCase());
                if (price != null) {
                    totalRevenue += price;
                }
            }
        }

        return new AdminDashboardResponse(
                totalUsers,
                packages.size(),
                bookings.size(),
                totalRevenue
        );
    }
}