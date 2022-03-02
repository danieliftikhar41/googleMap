package com.example.googlemap.url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUrl {
    public static final String url="https://www.flickr.com/services/rest/";
    public static Retrofit getConnect(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }
}
