package com.tripplanner.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripplanner.backend.dto.BookingRequestDto;
import com.tripplanner.backend.dto.BookingResponseDto;
import com.tripplanner.backend.exception.ResourceNotFoundException;
import com.tripplanner.backend.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@WithMockUser
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void createBooking_shouldReturnCreatedStatus() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto("John", "john@example.com", "Standard");
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard");

        when(bookingService.createBooking(any(BookingRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/bookings")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void getBookingById_shouldReturnOkStatus() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard");
        when(bookingService.getBookingById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getBookingById_whenNotFound_shouldReturn404() throws Exception {
        when(bookingService.getBookingById(999L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/bookings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void createBooking_withInvalidData_shouldReturn400() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto("", "invalid-email", "");
        
        mockMvc.perform(post("/api/bookings")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
