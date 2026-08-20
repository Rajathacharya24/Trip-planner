package com.tripplanner.backend.repository;

import com.tripplanner.backend.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    Page<Package> findByPriceLessThanEqual(int maxPrice, Pageable pageable);
}
