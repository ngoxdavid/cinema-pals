package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * TheatersResponse
 * - Response from API when requesting list of theaters
 */
public class TheatersResponse {

    @SerializedName("cinemas")
    private List<Theater> cinemas;


    public List<Theater> getTheatersResponse() {
        return cinemas;
    }

    public void setTheaterResults(List<Theater> results) {

        this.cinemas = results;
    }
}
