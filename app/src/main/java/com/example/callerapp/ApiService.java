package com.example.callerapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("validate")
    Call<ApiResponse> validateNumber(
            @Query("access_key") String apiKey,
            @Query("number") String phoneNumber
    );



}