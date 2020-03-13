package ru.otus.calleridclient.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import ru.otus.calleridclient.models.Caller;

@Dao
public interface CallerDao {
    @Query("SELECT * FROM caller")
    Flowable<List<Caller>> getAll();

    @Query("SELECT * FROM caller WHERE telephoneNumber = :telephoneNumber")
    Maybe<Caller> getById(String telephoneNumber);

    @Insert
    long insert(Caller caller);

    @Delete
    int delete(Caller caller);
}
