package ru.otus.calleridclient.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.SpamType;

public class CallerRepository {
    @Named("CacheDataStore")
    private final CallerDataStore cacheCallerDataStore;
    @Named("DatabaseDataStore")
    private final CallerDataStore databaseCallerDataStore;
    @Named("NetworkDataStore")
    private final CallerDataStore networkCallerDataStore;

    @Inject
    public CallerRepository(CacheCallerDataStore cacheCallerDataStore, DatabaseCallerDataStore databaseCallerDataStore
            , NetworkCallerDataStore networkCallerDataStore) {
        this.cacheCallerDataStore = cacheCallerDataStore;
        this.databaseCallerDataStore = databaseCallerDataStore;
        this.networkCallerDataStore = networkCallerDataStore;
    }

    private Observable<List<SpamType>> getSpamTypesFromMemory() {
        return cacheCallerDataStore.getSpamTypes();
    }

    private Observable<List<SpamType>> getSpamTypesFromDatabase() {
        return databaseCallerDataStore.getSpamTypes()
                .flatMap((Function<List<SpamType>, Observable<List<SpamType>>>) spamTypes -> {
                    if (spamTypes.size() == 0)
                        return Observable.create(Emitter::onComplete);
                    return Observable.fromArray(spamTypes);
                })
                .doOnNext(cacheCallerDataStore::saveSpamTypes);
    }

    private Observable<List<SpamType>> getSpamTypesFromNetwork() {
        return networkCallerDataStore.getSpamTypes()
                .doOnNext(spamTypes -> {
                    databaseCallerDataStore.saveSpamTypes(spamTypes);
                    cacheCallerDataStore.saveSpamTypes(spamTypes);
                });
    }

    private Observable<List<Caller>> getCallersFromMemory() {
        return cacheCallerDataStore.getCallers();
    }

    private Observable<List<Caller>> getCallersFromDB() {
        return databaseCallerDataStore.getCallers()
                .doOnNext(callers -> callers.forEach(caller -> {
                    cacheCallerDataStore.saveCaller(caller).subscribe();
                }));
    }

    public Observable<List<SpamType>> getSpamTypes() {
        return Observable.concat(getSpamTypesFromMemory(), getSpamTypesFromDatabase(), getSpamTypesFromNetwork())
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable();
    }

    public Observable<List<Caller>> getCallers() {
        return Observable.concat(getCallersFromMemory(), getCallersFromDB())
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable();
    }

    public Observable<Boolean> removeCaller(Caller caller) {
        return Observable.merge(cacheCallerDataStore.removeCaller(caller), databaseCallerDataStore.removeCaller(caller))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> createCaller(Caller caller) {
        return Observable.merge(databaseCallerDataStore.saveCaller(caller),
                cacheCallerDataStore.saveCaller(caller), networkCallerDataStore.saveCaller(caller))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
