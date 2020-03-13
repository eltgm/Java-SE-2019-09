package ru.otus.calleridclient.di;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.otus.calleridclient.data.AppDatabase;
import ru.otus.calleridclient.data.CallerDao;
import ru.otus.calleridclient.data.SpamTypeDao;

@Module
public class RoomModule {
    private AppDatabase appDatabase;

    public RoomModule(Context context) {
        this.appDatabase = Room.databaseBuilder(context, AppDatabase.class, "callerIDDB").build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDatabase;
    }

    @Singleton
    @Provides
    CallerDao providesCallerDao(AppDatabase demoDatabase) {
        return demoDatabase.callerDao();
    }

    @Singleton
    @Provides
    SpamTypeDao providesSpamTypeDao(AppDatabase demoDatabase) {
        return demoDatabase.spamTypeDao();
    }
}
