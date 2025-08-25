package com.crediya.auth.infrastructure.adapters.driven.persistence.repository;

import com.crediya.auth.infrastructure.adapters.driven.persistence.entity.UserData;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository interface for the UserData entity.
 */
@Repository
public interface UserDataRepository extends R2dbcRepository<UserData, Long> {

    /**
     * A custom query method to check for the existence of a user by their email.
     *
     * @param email The email to check for.
     * @return A Mono<Boolean> emitting true if the email exists, false otherwise.
     */
    Mono<Boolean> existsByEmail(String email);
}
