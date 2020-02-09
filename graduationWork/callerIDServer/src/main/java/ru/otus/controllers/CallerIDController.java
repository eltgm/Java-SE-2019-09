package ru.otus.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.models.Caller;
import ru.otus.services.CallerIDService;

@RestController
public class CallerIDController {
    private final CallerIDService callerIDService;

    public CallerIDController(CallerIDService callerIDService) {
        this.callerIDService = callerIDService;
    }

    @PostMapping("/caller/add")
    public boolean createNewCaller(Caller caller) {
        return callerIDService.createCaller(caller);
    }

    @GetMapping("/caller/get")
    public Caller createNewCaller(String telephoneNumber) {
        return callerIDService.getCallerByNumber(telephoneNumber);
    }
}
