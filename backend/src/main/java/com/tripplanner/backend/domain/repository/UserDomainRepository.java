package com.tripplanner.backend.domain.repository;

import com.tripplanner.backend.domain.model.User;
import java.util.Optional;

public interface UserDomainRepository {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    Optional<User> findById(Long id);
}
