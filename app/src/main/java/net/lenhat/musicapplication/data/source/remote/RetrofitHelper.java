package net.lenhat.musicapplication.data.source.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitHelper {
    public static AppService getInstance() {
        String baseUrl = "https://thantrieu.com";
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppService.class);
    }
}
