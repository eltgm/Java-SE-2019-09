package ru.otus.calleridclient.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.SpamType;

@Singleton
public class CacheCallerDataStore implements CallerDataStore {
    private final List<Caller> callers = new ArrayList<>();
    private List<SpamType> spamTypes;

    @Inject
    public CacheCallerDataStore() {
    }

    @Override
    public Observable<List<SpamType>> getSpamTypes() {
        return Observable.create(emitter -> {
            if (spamTypes != null) {
                emitter.onNext(spamTypes);
            }
            emitter.onComplete();
        });
    }

    @Override
    public void saveSpamTypes(List<SpamType> spamTypes) {
        this.spamTypes = spamTypes;
    }

    @Override
    public Observable<List<Caller>> getCallers() {
        return Observable.create(emitter -> {
            if (this.callers.size() > 0)
                emitter.onNext(this.callers);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Boolean> saveCaller(Caller caller) {
        return Observable.create(emitter -> {
            emitter.onNext(this.callers.add(caller));
            emitter.onComplete();
        });

    }

    @Override
    public Observable<Boolean> removeCaller(Caller caller) {
        return Observable.create(emitter -> {
            emitter.onNext(this.callers.remove(caller));
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Caller> getCallerById(String phoneNumber) {
        return Observable.empty();
    }
}
