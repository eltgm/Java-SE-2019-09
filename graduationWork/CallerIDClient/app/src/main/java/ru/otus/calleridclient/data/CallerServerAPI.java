package ru.otus.calleridclient.data;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.otus.calleridclient.models.Caller;
import ru.otus.calleridclient.models.Message;

public interface CallerServerAPI {
    @FormUrlEncoded
    @POST("caller/add")
    Observable<Message> createCaller(@Field("telephoneNumber") String telephoneNumber, @Field("spamCategories") String spamCategories,
                                     @Field("description") String description);

    @GET("caller/get")
    Observable<Caller> getCaller(String telephoneNumber);
}
