package ru.otus.calleridclient.repositories;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import ru.otus.calleridclient.data.CallerServerAPI;
import ru.otus.calleridclient.data.SpamTypeServerAPI;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.SpamType;

public class NetworkCallerDataStore implements CallerDataStore {
    private final CallerServerAPI callerServerAPI;
    private final SpamTypeServerAPI spamTypeServerAPI;

    @Inject
    public NetworkCallerDataStore(CallerServerAPI callerServerAPI, SpamTypeServerAPI spamTypeServerAPI) {
        this.callerServerAPI = callerServerAPI;
        this.spamTypeServerAPI = spamTypeServerAPI;
    }

    @Override
    public Observable<List<SpamType>> getSpamTypes() {
        return spamTypeServerAPI.getSpamCategories()
                .map(strings -> strings.stream()
                        .map(s -> SpamType.builder()
                                .type(s)
                                .build())
                        .collect(Collectors.toList()));
    }

    @Override
    public void saveSpamTypes(List<SpamType> spamTypes) {

    }

    @Override
    public Observable<List<Caller>> getCallers() {
        return null;
    }

    @Override
    public Observable<Boolean> saveCaller(Caller caller) {
        return Observable.create(emitter -> emitter.onNext(callerServerAPI.createCaller(caller)));
    }

    @Override
    public Observable<Boolean> removeCaller(Caller caller) {
        return Observable.create(Emitter::onComplete);
    }
}
