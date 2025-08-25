package com.crediya.auth.infrastructure.adapters.driven.persistence;


import com.crediya.auth.domain.model.User;
import com.crediya.auth.infrastructure.adapters.driven.persistence.entity.UserData;
import com.crediya.auth.infrastructure.adapters.driven.persistence.mapper.UserMapper;
import com.crediya.auth.infrastructure.adapters.driven.persistence.mapper.UserMapperImpl;
import com.crediya.auth.infrastructure.adapters.driven.persistence.repository.UserDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Integration tests for the UserRepositoryAdapter.
 * @DataR2dbcTest loads the persistence context, including an in-memory H2 database.
 */
@DataR2dbcTest
@Import(UserMapperImpl.class)
public class UserRepositoryAdapterTest {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private UserMapper userMapper;

    private UserRepositoryAdapter userRepositoryAdapter;


    @BeforeEach
    void setUp() {
        userDataRepository.deleteAll().block();
        userRepositoryAdapter = new UserRepositoryAdapter(userDataRepository, userMapper);
    }

    @Test
    void shouldSaveUserAndReturnDomainUserWithId() {

        User userToSave = User.newUser(
                "Larry",
                "Ramirez",
                "larry.ramirez11@outlook.com",
                "123456789",
                "3001234567",
                LocalDate.of(1995, 11, 11),
                "456 Oak Ave",
                "APPLICANT",
                new BigDecimal("5000000")
        );

        Mono<User> savedUserMono = userRepositoryAdapter.save(userToSave);

        StepVerifier.create(savedUserMono)
                .expectNextMatches(savedUser ->
                        savedUser.getId() != null &&
                                savedUser.getEmail().equals(userToSave.getEmail()) &&
                                savedUser.getFirstName().equals(userToSave.getFirstName())
                )
                .verifyComplete();
    }

    @Test
    void existsByEmailShouldReturnTrueWhenEmailExists() {

        UserData userData = UserData.builder()
                .firstName("Larry")
                .lastName("Ramirez")
                .email("larry.ramirez11@outlook.com")
                .baseSalary(new BigDecimal("5000000"))
                .build();

        Mono<Void> setup = userDataRepository.save(userData).then();

        Mono<Boolean> existsMono = userRepositoryAdapter.existsByEmail("larry.ramirez11@outlook.com");

        StepVerifier.create(setup.then(existsMono))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void existsByEmailShouldReturnFalseWhenEmailDoesNotExist() {

        Mono<Boolean> existsMono = userRepositoryAdapter.existsByEmail("nonexistent@example.com");

        StepVerifier.create(existsMono)
                .expectNext(false)
                .verifyComplete();
    }
}
