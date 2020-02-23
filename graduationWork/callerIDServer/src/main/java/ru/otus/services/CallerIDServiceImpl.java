package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.models.Caller;
import ru.otus.models.Message;
import ru.otus.repositories.CallersRepository;

@Service
public class CallerIDServiceImpl implements CallerIDService {
    private final CallersRepository callersRepository;

    public CallerIDServiceImpl(CallersRepository callersRepository) {
        this.callersRepository = callersRepository;
    }

    @Override
    public Message createCaller(Caller caller) {
        callersRepository.save(caller);

        return Message.builder()
                .status(true)
                .build();
    }

    @Override
    public Caller getCallerByNumber(String telephoneNumber) {
        final var caller = callersRepository.findByTelephoneNumber(telephoneNumber);

        return caller.orElseGet(Caller::new);
    }
}
