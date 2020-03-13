package ru.otus.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import ru.otus.models.SpamType;
import ru.otus.repositories.SpamCategoriesRepository;

import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpamCategoriesServiceTest {
    private static final SpamCategoriesRepository spamCategoriesRepository = mock(SpamCategoriesRepository.class);
    private static SpamCategoriesService spamCategoriesService;

    @BeforeAll
    public static void init() {
        spamCategoriesService = new SpamCategoriesServiceImpl(spamCategoriesRepository);
    }

    @DisplayName("Тестирование получения категорий в ситуациях, когда ")
    @ParameterizedTest(name = "{1}")
    @CsvSource({"true, категории существуют",
            "false, категорий нет"})
    public void getSpamTypes(ArgumentsAccessor arguments) {
        var typesExists = arguments.getBoolean(0);

        when(spamCategoriesRepository.findAll()).thenReturn(
                typesExists ? Collections.singletonList(SpamType.builder().build()) : Collections.emptyList()
        );

        final var spamTypes = spamCategoriesService.getSpamTypes();

        if (typesExists)
            assertNotEmpty(spamTypes, "Список не пуст");
        else
            assertEquals(spamTypes.size(), 0);
    }
}
