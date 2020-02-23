package ru.otus.services;

import ru.otus.models.Caller;
import ru.otus.models.Message;

public interface CallerIDService {
    Message createCaller(Caller caller);

    Caller getCallerByNumber(String telephoneNumber);
}
