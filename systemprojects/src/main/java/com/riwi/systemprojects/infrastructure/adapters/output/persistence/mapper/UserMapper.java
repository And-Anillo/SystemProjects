package com.riwi.systemprojects.infrastructure.adapters.output.persistence.mapper;

import com.riwi.systemprojects.domain.model.User;
import com.riwi.systemprojects.infrastructure.adapters.output.persistence.entity.UserEntity;

/**
 * Mapper between User domain model and UserEntity JPA entity.
 */
public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null)
            return null;
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword());
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null)
            return null;
        return new UserEntity(
                domain.getId(),
                domain.getUsername(),
                domain.getEmail(),
                domain.getPassword());
    }
}
