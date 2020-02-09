package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.models.SpamType;
import ru.otus.repositories.SpamCategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpamCategoriesServiceImpl implements SpamCategoriesService {
    private final SpamCategoriesRepository spamCategoriesRepository;

    public SpamCategoriesServiceImpl(SpamCategoriesRepository spamCategoriesRepository) {
        this.spamCategoriesRepository = spamCategoriesRepository;
    }

    @Override
    public List<String> getSpamTypes() {
        return spamCategoriesRepository.findAll()
                .parallelStream()
                .map(SpamType::getType)
                .collect(Collectors.toList());
    }
}
