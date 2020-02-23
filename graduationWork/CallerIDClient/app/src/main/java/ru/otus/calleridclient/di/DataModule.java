package ru.otus.calleridclient.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.otus.calleridclient.data.CallerDao;
import ru.otus.calleridclient.data.CallerServerAPI;
import ru.otus.calleridclient.data.SpamTypeDao;
import ru.otus.calleridclient.data.SpamTypeServerAPI;
import ru.otus.calleridclient.repositories.CacheCallerDataStore;
import ru.otus.calleridclient.repositories.CallerDataStore;
import ru.otus.calleridclient.repositories.DatabaseCallerDataStore;
import ru.otus.calleridclient.repositories.NetworkCallerDataStore;

@Module(includes = {RoomModule.class, NetworkModule.class})
class DataModule {
    @Provides
    @Singleton
    @Named("CacheDataStore")
    CallerDataStore provideCacheCallerDataStore() {
        return new CacheCallerDataStore();
    }

    @Provides
    @Singleton
    @Named("DatabaseDataStore")
    CallerDataStore provideDatabaseCallerDataStore(CallerDao callerDao, SpamTypeDao spamTypeDao) {
        return new DatabaseCallerDataStore(callerDao, spamTypeDao);
    }

    @Provides
    @Singleton
    @Named("NetworkDataStore")
    CallerDataStore provideNetworkCallerDataStore(CallerServerAPI callerServerAPI, SpamTypeServerAPI spamTypeServerAPI) {
        return new NetworkCallerDataStore(callerServerAPI, spamTypeServerAPI);
    }
}
