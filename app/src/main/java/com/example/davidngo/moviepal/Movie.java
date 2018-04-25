package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Movie Class
 * - Contains information about a specific movie
 */
public class Movie implements Serializable {


    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<>();
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("poster_image_thumbnail")
    private String poster_image;

    //Getters
    public Boolean getVideo() {
        return video;
    }

    public Integer getVoteCount() {

        return voteCount;
    }

    public Double getPopularity() {

        return popularity;
    }

    public String getBackdropPath() {

        return backdropPath;
    }

    public String getOriginalLanguage() {

        return originalLanguage;
    }

    public String getOriginalTitle() {

        return originalTitle;
    }

    public List<Integer> getGenreIds() {

        return genreIds;
    }

    public String getReleaseDate() {

        return releaseDate;
    }

    public boolean isAdult() {

        return adult;
    }

    public String getPoster_image() {
        return poster_image;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
