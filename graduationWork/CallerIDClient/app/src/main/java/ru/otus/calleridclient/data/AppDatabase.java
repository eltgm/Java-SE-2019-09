package ru.otus.calleridclient.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.SpamType;

@Database(entities = {Caller.class, SpamType.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CallerDao callerDao();

    public abstract SpamTypeDao spamTypeDao();
}
