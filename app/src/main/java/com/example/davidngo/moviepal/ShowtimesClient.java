package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * ShowtimesClient
 * - interface to setup making the HTTP Get Request to the API
 */
public interface ShowtimesClient {


    @GET("showtimes")
    Call<ShowtimesResponse> getNearbyShowtimes(
            @Query("apikey") String apikey,
            @Query("location") String location,
            @Query("time_from") String time_from,
            @Query("time_to") String time_to,
            @Query("movie_id") String movie_id,
            @Query("cinema_id") String cinema_id,
            @Query("append") String append);
}
