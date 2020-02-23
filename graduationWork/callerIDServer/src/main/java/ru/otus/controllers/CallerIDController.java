package ru.otus.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.models.Caller;
import ru.otus.models.CallerDTO;
import ru.otus.models.Message;
import ru.otus.services.CallerIDService;

@RestController
public class CallerIDController {
    private final CallerIDService callerIDService;
    private final ModelMapper modelMapper;

    public CallerIDController(CallerIDService callerIDService, ModelMapper modelMapper) {
        this.callerIDService = callerIDService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/caller/add")
    public Message addCaller(CallerDTO callerDTO) {
        final var caller = modelMapper.map(callerDTO, Caller.class);

        return callerIDService.createCaller(caller);
    }

    @GetMapping("/caller/get")
    public Caller getCaller(String telephoneNumber) {
        return callerIDService.getCallerByNumber(telephoneNumber);
    }
}
