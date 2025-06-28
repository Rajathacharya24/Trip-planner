package com.tripplanner.backend.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripplanner.backend.model.PackageInfo;

@RestController
@RequestMapping("/api/packages")
public class PackageController {
    @GetMapping
    public List<PackageInfo> getPackages() {
        return Arrays.asList(
            new PackageInfo("Ordinary Package", "Hotel Stay, Basic Transport, 2 Meals Daily", 15000),
            new PackageInfo("Luxury Package", "5-Star Hotel, Private Cab, All Meals Included", 35000)
        );
    }
}
