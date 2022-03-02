package com.example.googlemap;

import com.example.googlemap.Models.ModelApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {
    @GET("?method=flickr.photos.search&api_key=79d466885188b99d6762980d64029892&format=json&nojsoncallback=1")
    Call<ModelApi> getData(@Query("lat") double lat, @Query("lon") double lon);


}
