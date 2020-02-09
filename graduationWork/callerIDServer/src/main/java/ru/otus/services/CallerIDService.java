package ru.otus.services;

import ru.otus.models.Caller;

public interface CallerIDService {
    boolean createCaller(Caller caller);

    Caller getCallerByNumber(String telephoneNumber);
}
