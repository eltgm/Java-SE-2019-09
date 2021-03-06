package ru.otus.calleridclient.data;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface SpamTypeServerAPI {
    @GET("spam/categories")
    Observable<List<String>> getSpamCategories();
}
