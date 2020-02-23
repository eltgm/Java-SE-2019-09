package ru.otus.calleridclient.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.otus.calleridclient.data.CallerServerAPI;
import ru.otus.calleridclient.data.SpamTypeServerAPI;

@Module
public class NetworkModule {
    @Singleton
    @Provides
    Retrofit providesRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    SpamTypeServerAPI provideSpamTypeServerAPI(Retrofit retrofit) {
        return retrofit.create(SpamTypeServerAPI.class);
    }

    @Singleton
    @Provides
    CallerServerAPI provideCallerServerAPI(Retrofit retrofit) {
        return retrofit.create(CallerServerAPI.class);
    }
}
