package ru.otus.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.models.Caller;
import ru.otus.repositories.CallersRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallerIDServiceTest {
    private static final CallersRepository callersRepository = mock(CallersRepository.class);
    private static CallerIDService callerIDService;

    @BeforeAll
    public static void init() {
        callerIDService = new CallerIDServiceImpl(callersRepository);
    }

    @DisplayName("Тестирование создания абонента в ситуациях, когда ")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"true, абонент существует",
            "false, абонента нет в системе"})
    public void createCaller(ArgumentsAccessor arguments) {
        var userExists = arguments.getBoolean(0);

        when(callersRepository.findByTelephoneNumber(anyString()))
                .thenReturn(userExists ? Optional.of(Caller.builder().build()) : Optional.empty());

        var createResult = callerIDService.createCaller(Caller.builder()
                .telephoneNumber("")
                .build());

        assertTrue(createResult.isStatus());
    }

    @DisplayName("Тестирование получения абонента в ситуациях, когда ")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"true, абонент существует",
            "false, абонента нет в системе"})
    public void getCallerByNumber(ArgumentsAccessor arguments) {
        var userExists = arguments.getBoolean(0);

        when(callersRepository.findByTelephoneNumber(anyString()))
                .thenReturn(userExists ? Optional.of(Caller.builder().build()) : Optional.empty());

        if (userExists) {
            var getUserResult = callerIDService.getCallerByNumber(anyString());

            assertNotNull(getUserResult);
        } else {
            assertNull(callerIDService.getCallerByNumber(anyString()).getTelephoneNumber());
        }
    }
}
