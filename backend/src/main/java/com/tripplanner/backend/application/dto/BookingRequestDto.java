package com.tripplanner.backend.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class BookingRequestDto {
    @NotNull(message = "Package ID is required")
    private Long packageId;

    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    @NotNull(message = "Adults is required")
    @Positive(message = "Adults must be greater than 0")
    private Integer adults;

    @NotNull(message = "Children is required")
    @Min(value = 0, message = "Children cannot be negative")
    private Integer children;

    @NotNull(message = "Rooms is required")
    @Positive(message = "Rooms must be at least 1")
    private Integer rooms;

    public BookingRequestDto() {
    }

    public BookingRequestDto(Long packageId, LocalDate departureDate, LocalDate returnDate, Integer adults, Integer children, Integer rooms) {
        this.packageId = packageId;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.adults = adults;
        this.children = children;
        this.rooms = rooms;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }
}
