package com.tripplanner.backend.application.dto;

import com.tripplanner.backend.domain.model.BookingStatus;
import jakarta.validation.constraints.NotNull;

public class BookingStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private BookingStatus status;

    public BookingStatusUpdateRequest() {
    }

    public BookingStatusUpdateRequest(BookingStatus status) {
        this.status = status;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}