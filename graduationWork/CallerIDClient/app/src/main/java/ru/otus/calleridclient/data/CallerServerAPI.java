package ru.otus.calleridclient.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.otus.calleridclient.models.Caller;

public interface CallerServerAPI {
    @POST("caller/add")
    boolean createCaller(@Body Caller caller);

    @GET("caller/get")
    Call<Caller> getCaller(String telephoneNumber);
}
