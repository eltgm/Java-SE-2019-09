package ru.otus.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.services.SpamCategoriesService;

import java.util.List;

@RestController
public class SpamCategoriesController {
    private final SpamCategoriesService spamCategoriesService;

    public SpamCategoriesController(SpamCategoriesService spamCategoriesService) {
        this.spamCategoriesService = spamCategoriesService;
    }

    @GetMapping("/spam/categories")
    public List<String> getSpamCategories() {
        return spamCategoriesService.getSpamTypes();
    }
}
