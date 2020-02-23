package ru.otus.calleridclient.repositories;

import java.util.List;

import io.reactivex.Observable;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.SpamType;

public interface CallerDataStore {
    Observable<List<SpamType>> getSpamTypes();

    void saveSpamTypes(List<SpamType> spamTypes);

    Observable<List<Caller>> getCallers();

    Observable<Boolean> saveCaller(Caller caller);

    Observable<Boolean> removeCaller(Caller caller);
}
