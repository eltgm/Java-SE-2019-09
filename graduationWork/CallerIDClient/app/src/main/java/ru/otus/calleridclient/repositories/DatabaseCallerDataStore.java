package ru.otus.calleridclient.repositories;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.otus.calleridclient.data.CallerDao;
import ru.otus.calleridclient.data.SpamTypeDao;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.SpamType;

public class DatabaseCallerDataStore implements CallerDataStore {
    private final CallerDao callerDao;
    private final SpamTypeDao spamTypeDao;

    @Inject
    public DatabaseCallerDataStore(CallerDao callerDao, SpamTypeDao spamTypeDao) {
        this.callerDao = callerDao;
        this.spamTypeDao = spamTypeDao;
    }

    @Override
    public Observable<List<SpamType>> getSpamTypes() {
        return spamTypeDao.getAll().toObservable();
    }

    @Override
    public void saveSpamTypes(List<SpamType> spamTypes) {
        spamTypes.forEach(spamTypeDao::insert);
    }

    @Override
    public Observable<List<Caller>> getCallers() {
        return callerDao.getAll().toObservable();
    }

    @Override
    public Observable<Boolean> saveCaller(Caller caller) {
        return Observable.create(emitter -> {
            long insert = 0;
            try {
                insert = callerDao.insert(caller);
            } catch (Exception exc) {
                emitter.onError(exc);
            }

            caller.setId(insert);
            emitter.onNext(insert > 0);

            emitter.onComplete();
        });
    }

    @Override
    public Observable<Boolean> removeCaller(Caller caller) {
        return Observable.create(emitter -> {
            emitter.onNext(callerDao.delete(caller) > 0);
            emitter.onComplete();
        });
    }
}
