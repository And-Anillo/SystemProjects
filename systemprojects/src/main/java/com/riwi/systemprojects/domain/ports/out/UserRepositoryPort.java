package com.riwi.systemprojects.domain.ports.out;

import com.riwi.systemprojects.domain.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for User persistence operations.
 * This interface defines the contract that infrastructure adapters must
 * implement.
 */
public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
