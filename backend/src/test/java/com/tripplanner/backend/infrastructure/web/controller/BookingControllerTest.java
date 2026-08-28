package com.tripplanner.backend.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripplanner.backend.application.dto.BookingRequestDto;
import com.tripplanner.backend.application.dto.BookingResponseDto;
import com.tripplanner.backend.domain.exception.ResourceNotFoundException;
import com.tripplanner.backend.application.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.tripplanner.backend.infrastructure.config.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@Import(SecurityConfig.class)
@WithMockUser
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private com.tripplanner.backend.infrastructure.security.JwtUtils jwtUtils;

    @MockBean
    private com.tripplanner.backend.infrastructure.security.CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "john@example.com")
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
    @WithMockUser(username = "john@example.com")
    public void getBookingById_shouldReturnOkStatus() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard");
        when(bookingService.getBookingById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    public void getBookingById_shouldIncludeSecurityHeaders() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard");
        when(bookingService.getBookingById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Referrer-Policy", "no-referrer"))
                .andExpect(header().string("Permissions-Policy", "camera=(), microphone=(), geolocation=()"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
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

    @Test
    @WithMockUser(username = "invalid@example.com")
    public void getBookingById_whenUserDoesNotOwnBooking_shouldReturnForbidden() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard");
        when(bookingService.getBookingById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void getBookingById_whenAdminAccessesOtherBooking_shouldAllow() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard");
        when(bookingService.getBookingById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john@example.com")
    public void updateBookingStatus_whenUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/bookings/1/status")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void updateBookingStatus_whenAdmin_shouldAllow() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(1L, "John", "john@example.com", "Standard", "CONFIRMED");
        when(bookingService.updateBookingStatus(any(Long.class), any(String.class))).thenReturn(responseDto);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/bookings/1/status")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}
