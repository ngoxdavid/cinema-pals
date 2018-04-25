package com.example.davidngo.moviepal;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

/**
 * MoviesResponse
 * - After making a GET request to the API, the API will respond
 * with a JSON object that will contain these fields
 * It will also contain a List of Movies which is what we are mainly aiming for
 */
public class MoviesResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> movieResults;
    @SerializedName("total_results")
    private int totalMovieResults;
    @SerializedName("total_pages")
    private int totalPages;

    public List<Movie> getMovieResults() {

        return movieResults;
    }

    public void setMovieResults(List<Movie> results) {

        this.movieResults = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public int getTotalMovieResults() {

        return totalMovieResults;
    }

    public void setTotalMovieResults(int totalResults) {
        this.totalMovieResults = totalResults;
    }

    public int getTotalPages() {

        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
