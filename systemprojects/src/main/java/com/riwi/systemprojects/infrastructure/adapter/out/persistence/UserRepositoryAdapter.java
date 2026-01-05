package com.riwi.systemprojects.infrastructure.adapter.out.persistence;

import com.riwi.systemprojects.application.port.out.UserRepositoryPort;
import com.riwi.systemprojects.domain.model.User;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.riwi.systemprojects.infrastructure.adapter.out.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that implements UserRepositoryPort using JPA.
 * This is part of the infrastructure layer.
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = jpaUserRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
