package ru.otus.calleridclient.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.otus.calleridclient.R;
import ru.otus.calleridclient.data.CallerServerAPI;
import ru.otus.calleridclient.data.SpamTypeServerAPI;

@Module
public class NetworkModule {
    @Singleton
    @Provides
    Retrofit providesRetrofit(Context context) {
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_url))
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
