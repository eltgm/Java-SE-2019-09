package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.models.Caller;
import ru.otus.repositories.CallersRepository;

@Service
public class CallerIDServiceImpl implements CallerIDService {
    private final CallersRepository callersRepository;

    public CallerIDServiceImpl(CallersRepository callersRepository) {
        this.callersRepository = callersRepository;
    }

    @Override
    public boolean createCaller(Caller caller) {
        if (callersRepository.findByTelephoneNumber(caller.getTelephoneNumber()).isPresent()) {
            return false;
        }

        callersRepository.save(caller);

        return true;
    }

    @Override
    public Caller getCallerByNumber(String telephoneNumber) {
        final var caller = callersRepository.findByTelephoneNumber(telephoneNumber);

        return caller.orElseGet(Caller::new);
    }
}
