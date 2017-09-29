package com.hap.popularmovie.dagger.module;

import com.hap.popularmovie.MovieApplication;
import com.hap.popularmovie.dagger.scope.ApplicationScope;
import com.hap.popularmovie.network.MovieRestApi;
import com.hap.popularmovie.network.MovieRestService;
import com.hap.popularmovie.util.MovieSettings;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luis on 9/22/17.
 */

@Module
public class NetworkModule {
    private <T> T getObservableAdapter(final OkHttpClient okHttpClient, final Class<T> clazz) {
        return new Retrofit.Builder()
                .baseUrl(MovieSettings.getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(clazz);
    }

    @Provides
    @ApplicationScope
    protected OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }

    @Provides
    @ApplicationScope
    protected MovieRestService provideMovieRestSevice(final OkHttpClient okHttpClient) {
        final MovieRestApi movieRestApi = getObservableAdapter(okHttpClient, MovieRestApi.class);
        return new MovieRestService(MovieApplication.getInstance(), movieRestApi);
    }
}