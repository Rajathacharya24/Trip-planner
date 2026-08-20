package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.PackageRequestDto;
import com.tripplanner.backend.application.dto.PackageResponseDto;
import com.tripplanner.backend.domain.exception.ResourceNotFoundException;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PackageService {

    private static final Logger log = LoggerFactory.getLogger(PackageService.class);
    private final PackageDomainRepository packageRepository;

    public PackageService(PackageDomainRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Transactional
    public PackageResponseDto createPackage(PackageRequestDto requestDto) {
        log.info("Creating new package: {}", requestDto.getName());
        Package pkg = new Package(null, requestDto.getName(), requestDto.getDescription(), requestDto.getPrice());
        Package savedPkg = packageRepository.save(pkg);
        log.info("Package created successfully with ID: {}", savedPkg.getId());
        return mapToDto(savedPkg);
    }

    public Page<PackageResponseDto> getAllPackages(Pageable pageable) {
        log.debug("Fetching all packages with pagination: {}", pageable);
        return packageRepository.findAll(pageable).map(this::mapToDto);
    }

    public PackageResponseDto getPackageById(Long id) {
        log.debug("Fetching package by ID: {}", id);
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Package not found with ID: {}", id);
                    return new ResourceNotFoundException("Package not found with id: " + id);
                });
        return mapToDto(pkg);
    }

    @Transactional
    public PackageResponseDto updatePackage(Long id, PackageRequestDto requestDto) {
        log.info("Updating package with ID: {}", id);
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        pkg.setName(requestDto.getName());
        pkg.setDescription(requestDto.getDescription());
        pkg.setPrice(requestDto.getPrice());
        
        Package updatedPkg = packageRepository.save(pkg);
        log.info("Package updated successfully for ID: {}", id);
        return mapToDto(updatedPkg);
    }

    @Transactional
    public void deletePackage(Long id) {
        log.info("Deleting package with ID: {}", id);
        if (!packageRepository.existsById(id)) {
            log.warn("Cannot delete. Package not found with ID: {}", id);
            throw new ResourceNotFoundException("Package not found with id: " + id);
        }
        packageRepository.deleteById(id);
        log.info("Package deleted successfully for ID: {}", id);
    }

    public Page<PackageResponseDto> searchPackagesByMaxPrice(int maxPrice, Pageable pageable) {
        log.debug("Searching packages with price less than or equal to: {}", maxPrice);
        return packageRepository.findByPriceLessThanEqual(maxPrice, pageable).map(this::mapToDto);
    }

    private PackageResponseDto mapToDto(Package pkg) {
        return new PackageResponseDto(
                pkg.getId(),
                pkg.getName(),
                pkg.getDescription(),
                pkg.getPrice()
        );
    }
}
