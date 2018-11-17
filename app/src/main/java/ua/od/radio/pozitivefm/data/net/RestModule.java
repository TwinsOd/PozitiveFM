package ua.od.radio.pozitivefm.data.net;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.od.radio.pozitivefm.BuildConfig;


public class RestModule {
    private static final int TIMEOUT = 10;
    private static final int MAX_SIZE = 4 * 1024 * 1024;
    private static final String HTTP_CACHE = "http-cache";
    @NonNull
    private final OkHttpClient.Builder httpClientBuilder;

    public RestModule(@NonNull Context context) {
        httpClientBuilder = new OkHttpClient.Builder().readTimeout(TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        if (BuildConfig.DEBUG)
            httpClientBuilder.addInterceptor(logging);
        Cache cache = new Cache(new File(context.getCacheDir(), HTTP_CACHE), MAX_SIZE);
        httpClientBuilder.cache(cache);
    }

    //https://firebasestorage.googleapis.com/v0/b/meeter-b58ce.appspot.com/o/settings.json?alt=media&token=74974f7f-21c7-4ae9-ad44-f89fe99c22b0
    public RestApi provideFirebaseRestApi() {
        return new Retrofit.Builder()
                .baseUrl("https://firebasestorage.googleapis.com")
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApi.class);
    }

    public RestApi provideRestApi() {
        return new Retrofit.Builder()
                .baseUrl("http://pozitiv.fm/")
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApi.class);
    }
}
