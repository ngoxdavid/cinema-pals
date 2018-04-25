package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * TheaterClient
 * - Used to make a GET request to the API to get nearby theaters
 */
public interface TheaterClient {

    @GET("cinemas/")
    Call<TheatersResponse> getNearbyTheaters(
            @Query("apikey") String apikey,
            @Query("location") String location);
}
