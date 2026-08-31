package com.tripplanner.backend.application.service;

import com.tripplanner.backend.application.dto.PackageRequestDto;
import com.tripplanner.backend.application.dto.PackageResponseDto;
import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTest {

    @Mock
    private PackageDomainRepository packageRepository;

    @InjectMocks
    private PackageService packageService;

    @Test
    public void createPackage_shouldReturnPackageResponseDto() {
        PackageRequestDto requestDto = new PackageRequestDto("Budget", "Kerala", "Cheap", 1000);
        Package savedPackage = new Package(1L, "Budget", "Kerala", "Cheap", 1000);

        when(packageRepository.save(any(Package.class))).thenReturn(savedPackage);

        PackageResponseDto responseDto = packageService.createPackage(requestDto);

        assertEquals("Budget", responseDto.getName());
        assertEquals("Kerala", responseDto.getDestination());
        assertEquals("Cheap", responseDto.getDescription());
        assertEquals(1000, responseDto.getPrice());
        assertEquals(1L, responseDto.getId());
    }
}
