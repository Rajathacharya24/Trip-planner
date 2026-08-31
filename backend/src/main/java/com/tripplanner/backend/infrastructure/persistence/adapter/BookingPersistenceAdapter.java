package com.tripplanner.backend.infrastructure.persistence.adapter;

import com.tripplanner.backend.domain.model.Booking;
import com.tripplanner.backend.domain.model.BookingStatus;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.model.User;
import com.tripplanner.backend.domain.repository.BookingDomainRepository;
import com.tripplanner.backend.infrastructure.persistence.entity.BookingEntity;
import com.tripplanner.backend.infrastructure.persistence.entity.PackageEntity;
import com.tripplanner.backend.infrastructure.persistence.entity.UserEntity;
import com.tripplanner.backend.infrastructure.persistence.repository.SpringDataBookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@SuppressWarnings("null")
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
        return bookingRepository.findByUser_EmailContainingIgnoreCase(email, pageable).map(this::toDomain);
    }

    @Override
    public Page<Booking> findByUserId(Long userId, Pageable pageable) {
        return bookingRepository.findByUser_Id(userId, pageable).map(this::toDomain);
    }

    private BookingEntity toEntity(Booking booking) {
        BookingEntity entity = new BookingEntity();
        entity.setId(booking.getId());
        entity.setUser(toUserEntity(booking.getUser()));
        entity.setTravelPackage(toPackageEntity(booking.getTravelPackage()));
        entity.setDestination(booking.getDestination());
        entity.setDepartureDate(booking.getDepartureDate());
        entity.setReturnDate(booking.getReturnDate());
        entity.setAdults(booking.getAdults());
        entity.setChildren(booking.getChildren());
        entity.setRooms(booking.getRooms());
        entity.setTotalAmount(booking.getTotalAmount());
        entity.setStatus(booking.getStatus() == null ? BookingStatus.PENDING : booking.getStatus());
        entity.setCreatedAt(booking.getCreatedAt());
        entity.setUpdatedAt(booking.getUpdatedAt());
        return entity;
    }

    private Booking toDomain(BookingEntity entity) {
        return new Booking(
                entity.getId(),
                toDomainUser(entity.getUser()),
                toDomainPackage(entity.getTravelPackage()),
                entity.getDestination(),
                entity.getDepartureDate(),
                entity.getReturnDate(),
                entity.getAdults(),
                entity.getChildren(),
                entity.getRooms(),
                entity.getTotalAmount(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private UserEntity toUserEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    private PackageEntity toPackageEntity(Package travelPackage) {
        if (travelPackage == null) {
            return null;
        }

        PackageEntity entity = new PackageEntity();
        entity.setId(travelPackage.getId());
        entity.setName(travelPackage.getName());
        entity.setDestination(travelPackage.getDestination());
        entity.setDescription(travelPackage.getDescription());
        entity.setPrice(travelPackage.getPrice());
        return entity;
    }

    private User toDomainUser(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Package toDomainPackage(PackageEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Package(
                entity.getId(),
                entity.getName(),
                entity.getDestination(),
                entity.getDescription(),
                entity.getPrice()
        );
    }
}
