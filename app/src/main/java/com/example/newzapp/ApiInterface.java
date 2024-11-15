package com.example.newzapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(@Query("country") String country, @Query("apiKey") String apiKey, @Query("category") String category);
}
