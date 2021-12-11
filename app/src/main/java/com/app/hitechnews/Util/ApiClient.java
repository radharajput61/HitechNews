package com.app.hitechnews.Util;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .readTimeout(100, TimeUnit.SECONDS)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .build();
//            http://abdekhegabharat.sigmasoftwares.co/
            ////////////////////////////////////////////////////
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://hitechnews.sigmasoftwares.co/")
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setLenient()
                            .create()))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    private static final String BASE_URL = "http://filecheck.sigmasoftwares.co/api/";



    public static Retrofit getRetrofitInstance() {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
