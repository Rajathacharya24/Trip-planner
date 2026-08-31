package com.tripplanner.backend.infrastructure.persistence.adapter;

import com.tripplanner.backend.domain.model.Package;
import com.tripplanner.backend.domain.repository.PackageDomainRepository;
import com.tripplanner.backend.infrastructure.persistence.entity.PackageEntity;
import com.tripplanner.backend.infrastructure.persistence.repository.SpringDataPackageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@SuppressWarnings("null")
public class PackagePersistenceAdapter implements PackageDomainRepository {

    private final SpringDataPackageRepository packageRepository;

    public PackagePersistenceAdapter(SpringDataPackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Package save(Package pkg) {
        PackageEntity entity = toEntity(pkg);
        PackageEntity savedEntity = packageRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Page<Package> findAll(Pageable pageable) {
        return packageRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Optional<Package> findById(Long id) {
        return packageRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return packageRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        packageRepository.deleteById(id);
    }

    @Override
    public Page<Package> findByPriceLessThanEqual(int maxPrice, Pageable pageable) {
        return packageRepository.findByPriceLessThanEqual(maxPrice, pageable).map(this::toDomain);
    }

    private PackageEntity toEntity(Package pkg) {
        return new PackageEntity(
                pkg.getId(),
                pkg.getName(),
                pkg.getDestination(),
                pkg.getDescription(),
                pkg.getPrice()
        );
    }

    private Package toDomain(PackageEntity entity) {
        return new Package(
                entity.getId(),
                entity.getName(),
                entity.getDestination(),
                entity.getDescription(),
                entity.getPrice()
        );
    }
}
