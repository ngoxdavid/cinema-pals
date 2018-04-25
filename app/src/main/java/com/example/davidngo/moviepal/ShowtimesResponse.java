package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * ShowtimesResponse
 * - repsonse for getting showtimes from API
 * - contains showtimes, movies, and theaters
 */
public class ShowtimesResponse {

    @SerializedName("showtimes")
    private List<Showtime> showtimes;

    @SerializedName("movies")
    private List<Movie> movies;

    @SerializedName("cinemas")
    private List<Theater> cinemas;


    public List<Theater> getTheaters() {
        return cinemas;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    public void setMovies(List<Movie> movies) {

        this.movies = movies;
    }

    public void setTheaters(List<Theater> cinemas) {

        this.cinemas = cinemas;
    }

    public List<Showtime> getShowtimes() {

        return showtimes;
    }

    public List<Movie> getMovies() {

        return movies;
    }
}

