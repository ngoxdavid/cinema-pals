package com.example.davidngo.moviepal;

/**
 * App Name: Cinema Pals
 * Created by: David Ngo
 */


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * Theater Class
 * - Contains id, name, telephone,
 * - website, address, and a location object
 */
public class Theater implements Serializable {


    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("telephone")
    private String telephone;
    @SerializedName("website")
    private String website;
    @SerializedName("address")
    private String address;
    @SerializedName("location")
    private LocationObj location;

    public LocationObj getLocation() {
        return location;
    }

    /**
     * Location Object
     * - Has address, lat and longitude of the theater
     */
    class LocationObj implements Serializable {
        @SerializedName("lat")
        private String lat;
        @SerializedName("lon")
        private String lon;
        @SerializedName("address")
        private AddressObj address;

        public AddressObj getAddress() {
            return address;
        }

        class AddressObj implements Serializable {
            @SerializedName("house")
            private String house;
            @SerializedName("street")
            private String street;
            @SerializedName("city")
            private String city;
            @SerializedName("state_abbr")
            private String state_abbr;
            @SerializedName("zipcode")
            private String zipcode;

            public String getAddr() {
                return house + " " + street + ", " + city + ", " + state_abbr + " " + zipcode;
            }
        }
    }

    //Getters

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    public String getAddress() {
        return address;
    }

    private List<Showtime> showtimes;
    private List<Movie> moviesPlaying;


    public List<Movie> getMoviesPlaying() {
        return moviesPlaying;
    }

    public Movie getMovieWithID(int id) {
        Movie target = null;
        for (int i = 0; i < moviesPlaying.size(); i++) {
            target = moviesPlaying.get(i);
            if (target.getId() == id) {
                return target;
            }
        }
        return target;
    }

    public void setMoviesPlaying(List<Movie> moviesPlaying) {
        this.moviesPlaying = moviesPlaying;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getTelephone() {
        return telephone;
    }
}

