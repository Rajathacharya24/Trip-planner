package com.tripplanner.backend.infrastructure.persistence.adapter;

import com.tripplanner.backend.domain.model.User;
import com.tripplanner.backend.domain.repository.UserDomainRepository;
import com.tripplanner.backend.infrastructure.persistence.entity.UserEntity;
import com.tripplanner.backend.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserDomainRepository {

    private final SpringDataUserRepository repository;

    public UserPersistenceAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public long count() {
        return repository.count();
    }

    private User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return new UserEntity(
            domain.getId(),
            domain.getName(),
            domain.getEmail(),
            domain.getPassword(),
            domain.getRole(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
    }
}
