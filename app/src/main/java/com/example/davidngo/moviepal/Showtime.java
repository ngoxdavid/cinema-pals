package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import com.google.gson.annotations.SerializedName;

/**
 * Showtime class
 * - contains information about a showtime
 * - time the movie starts at, what movie it is and what theater it is playing at
 */
public class Showtime {

    @SerializedName("id")
    private String id;
    @SerializedName("cinema_id")
    private Integer cinemaID;
    @SerializedName("movie_id")
    private Integer movieID;
    @SerializedName("start_at")
    private String startTime;
    @SerializedName("language")
    private String language;


    public String getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public Integer getMovieID() {

        return movieID;
    }

    public Integer getCinemaID() {

        return cinemaID;
    }
}
