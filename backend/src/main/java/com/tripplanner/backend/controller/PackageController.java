package com.tripplanner.backend.controller;

import com.tripplanner.backend.dto.PackageRequestDto;
import com.tripplanner.backend.dto.PackageResponseDto;
import com.tripplanner.backend.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Tag(name = "Package Controller", description = "Endpoints for managing packages")
public class PackageController {

    private final PackageService packageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new package")
    public PackageResponseDto createPackage(@Valid @RequestBody PackageRequestDto requestDto) {
        log.info("Received request to create package");
        return packageService.createPackage(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all packages with pagination")
    public Page<PackageResponseDto> getAllPackages(Pageable pageable) {
        log.info("Received request to get all packages");
        return packageService.getAllPackages(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get package by ID")
    public PackageResponseDto getPackageById(@PathVariable Long id) {
        log.info("Received request to get package by ID: {}", id);
        return packageService.getPackageById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing package")
    public PackageResponseDto updatePackage(@PathVariable Long id, @Valid @RequestBody PackageRequestDto requestDto) {
        log.info("Received request to update package with ID: {}", id);
        return packageService.updatePackage(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a package")
    public void deletePackage(@PathVariable Long id) {
        log.info("Received request to delete package with ID: {}", id);
        packageService.deletePackage(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search packages by maximum price")
    public Page<PackageResponseDto> searchPackages(@RequestParam int maxPrice, Pageable pageable) {
        log.info("Received request to search packages with max price: {}", maxPrice);
        return packageService.searchPackagesByMaxPrice(maxPrice, pageable);
    }
}
