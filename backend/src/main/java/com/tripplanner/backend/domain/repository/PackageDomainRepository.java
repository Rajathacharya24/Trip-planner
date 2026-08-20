package com.tripplanner.backend.domain.repository;

import com.tripplanner.backend.domain.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PackageDomainRepository {
    Package save(Package pkg);
    Page<Package> findAll(Pageable pageable);
    Optional<Package> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
    Page<Package> findByPriceLessThanEqual(int price, Pageable pageable);
}
