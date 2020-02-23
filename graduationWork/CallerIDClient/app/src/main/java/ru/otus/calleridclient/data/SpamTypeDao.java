package ru.otus.calleridclient.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.otus.calleridclient.models.SpamType;

@Dao
public interface SpamTypeDao {
    @Query("SELECT * FROM spamtype")
    Single<List<SpamType>> getAll();

    @Insert
    void insert(SpamType caller);
}
