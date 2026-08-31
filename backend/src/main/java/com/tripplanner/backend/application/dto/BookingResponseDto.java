package com.tripplanner.backend.application.dto;

import com.tripplanner.backend.domain.model.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long id;
    private String customerName;
    private String customerEmail;
    private Long packageId;
    private String packageName;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer adults;
    private Integer children;
    private Integer rooms;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime createdAt;

    public BookingResponseDto() {
    }

    public BookingResponseDto(Long id,
                              String customerName,
                              String customerEmail,
                              Long packageId,
                              String packageName,
                              String destination,
                              LocalDate departureDate,
                              LocalDate returnDate,
                              Integer adults,
                              Integer children,
                              Integer rooms,
                              BigDecimal totalAmount,
                              BookingStatus status,
                              LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.packageId = packageId;
        this.packageName = packageName;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.adults = adults;
        this.children = children;
        this.rooms = rooms;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public BookingResponseDto(Long id, String customerName, String customerEmail, String packageName) {
        this(id, customerName, customerEmail, null, packageName, null, null, null, null, null, null, null, BookingStatus.PENDING, null);
    }

    public BookingResponseDto(Long id, String customerName, String customerEmail, String packageName, String status) {
        this(id, customerName, customerEmail, null, packageName, null, null, null, null, null, null, null, BookingStatus.valueOf(status), null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
