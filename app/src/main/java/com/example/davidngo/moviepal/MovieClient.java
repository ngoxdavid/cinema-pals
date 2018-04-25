package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * MovieClient - interface that handles the API Requests
 * - It has methods that will Call the API looking for a response that contains information
 * about movies
 */
public interface MovieClient {

    /**
     * Gets Movie By Title
     *
     * @param api_key  Cinema Pal Developer's API Key
     * @param language Language of the movie
     * @param region   Region that the movie is playing in
     * @param query    Query (In this case the Movie's Title)
     * @param year     The Year of the Movie
     * @return a Call object that will Call the API and receive a MovieResponse
     */
    @GET("search/movie")
    Call<MoviesResponse> getMovieByTitle(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("region") String region,
            @Query("query") String query,
            @Query("year") Integer year);

    /**
     * Grabs top rated movies of all time
     *
     * @param api_key Cinema Pal Developer's API Key
     * @return a Call object that will Call the API and receive a MovieResponse
     */
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRated(
            @Query("api_key") String api_key);

    /**
     * Gets movies that are coming out soon
     *
     * @param api_key  Cinema Pal Developer's API Key
     * @param language the language of the movie
     * @param region   region the movie is playing in
     * @return a Call object that will Call the API and receive a MovieResponse
     */
    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcoming(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("region") String region);

    /**
     * Gets Movies that are currently in theaters
     *
     * @param api_key  Cinema Pal Developer's API Key
     * @param language language of the movie
     * @param region   specifies the country that the movie is playing in
     * @return a Call object that will Call the API and receive a MovieResponse
     */
    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlaying(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("region") String region);

}
