package com.example.oims.security.infrastructure.persistence;

import com.example.oims.security.domain.model.User;
import com.example.oims.security.domain.model.UserRole;

public class UserMapper {
    public static User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                UserRole.valueOf(entity.getRole()),
                entity.getCreatedAt()
        );
    }

    public static UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
