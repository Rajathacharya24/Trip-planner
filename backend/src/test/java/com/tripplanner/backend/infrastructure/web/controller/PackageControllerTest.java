package com.tripplanner.backend.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripplanner.backend.application.dto.PackageRequestDto;
import com.tripplanner.backend.application.dto.PackageResponseDto;
import com.tripplanner.backend.application.service.PackageService;
import com.tripplanner.backend.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PackageController.class)
@Import(SecurityConfig.class)
@SuppressWarnings("null")
public class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PackageService packageService;

    @MockBean
    private com.tripplanner.backend.infrastructure.security.JwtUtils jwtUtils;

    @MockBean
    private com.tripplanner.backend.infrastructure.security.CustomUserDetailsService customUserDetailsService;

    @Test
    public void noJwt_shouldRejectAdminPackageCreate() throws Exception {
        PackageRequestDto requestDto = new PackageRequestDto("Budget", "Kerala", "Cheap", 1000);

        mockMvc.perform(post("/api/packages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void user_shouldNotCreatePackage() throws Exception {
        PackageRequestDto requestDto = new PackageRequestDto("Budget", "Kerala", "Cheap", 1000);

        mockMvc.perform(post("/api/packages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void admin_shouldCreatePackage() throws Exception {
        PackageRequestDto requestDto = new PackageRequestDto("Budget", "Kerala", "Cheap", 1000);
        PackageResponseDto responseDto = new PackageResponseDto(1L, "Budget", "Kerala", "Cheap", 1000);
        when(packageService.createPackage(any(PackageRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/packages")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void user_shouldNotDeletePackage() throws Exception {
        mockMvc.perform(delete("/api/packages/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void admin_shouldUpdatePackage() throws Exception {
        PackageRequestDto requestDto = new PackageRequestDto("Updated", "Kerala", "Better", 2000);
        PackageResponseDto responseDto = new PackageResponseDto(1L, "Updated", "Kerala", "Better", 2000);
        when(packageService.updatePackage(any(Long.class), any(PackageRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/packages/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void admin_shouldDeletePackage() throws Exception {
        mockMvc.perform(delete("/api/packages/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void admin_shouldViewPackages() throws Exception {
        when(packageService.getPackageById(1L)).thenReturn(new PackageResponseDto(1L, "Budget", "Kerala", "Cheap", 1000));

        mockMvc.perform(get("/api/packages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}