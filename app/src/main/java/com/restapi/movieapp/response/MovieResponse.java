package com.restapi.movieapp.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.restapi.movieapp.models.MovieModel;

public class MovieResponse {

    //this class is for single movie request
    //finding the movie object
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie() {
        return movie;
    }
}
