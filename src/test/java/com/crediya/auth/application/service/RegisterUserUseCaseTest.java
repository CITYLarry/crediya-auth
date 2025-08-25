package com.crediya.auth.application.service;

import com.crediya.auth.application.exceptions.EmailAlreadyExistsException;
import com.crediya.auth.application.ports.in.RegisterUserCommand;
import com.crediya.auth.domain.model.User;
import com.crediya.auth.domain.ports.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the RegisterUserUseCase.
 *
 * @ExtendWith(MockitoExtension.class) enables Mockito annotations.
 */
@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private RegisterUserCommand command;
    private User user;

    @BeforeEach
    void setUp() {
        command = new RegisterUserCommand(
                "Larry",
                "Ramirez",
                "larry.ramirez11@outlook.com",
                "123456789",
                "3001234567",
                LocalDate.of(1995, 11, 11),
                "123 Main St",
                "ROLE_USER",
                new BigDecimal("5000000")
        );

        user = command.toDomainUser();
    }


    @Test
    void shouldRegisterUserSuccessfullyWhenEmailDoesNotExist() {

        when(userRepository.existsByEmail(command.email())).thenReturn(Mono.just(false));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        Mono<User> result = registerUserUseCase.registerUser(command);

        StepVerifier.create(result)
                .expectNextMatches(savedUser ->
                        savedUser.getFirstName().equals(command.firstName()) &&
                                savedUser.getLastName().equals(command.lastName()) &&
                                savedUser.getEmail().equals(command.email()) &&
                                savedUser.getIdentityNumber().equals(command.identityNumber()) &&
                                savedUser.getPhoneNumber().equals(command.phoneNumber()) &&
                                savedUser.getBirthDate().equals(command.birthDate()) &&
                                savedUser.getAddress().equals(command.address()) &&
                                savedUser.getIdRole().equals(command.idRole()) &&
                                savedUser.getBaseSalary().compareTo(command.baseSalary()) == 0
                )
                .verifyComplete();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldReturnErrorWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(command.email())).thenReturn(Mono.just(true));

        Mono<User> result = registerUserUseCase.registerUser(command);

        StepVerifier.create(result)
                .expectError(EmailAlreadyExistsException.class)
                .verify();

        verify(userRepository, never()).save(any(User.class));
    }
}
