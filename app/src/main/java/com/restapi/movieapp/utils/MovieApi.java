package com.restapi.movieapp.utils;

import com.restapi.movieapp.models.MovieModel;
import com.restapi.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //search for movies
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher

    @GET("3/search/movie?")
    Call<MovieSearchResponse> searchMovie(

            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    //Get popular movies
    //https://api.themoviedb.org/3/movie/popular?api_key=510e67617f4ffb3cd980b3075e2ce149&page=1

    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );

    //making search with ID
    //https://api.themoviedb.org/3/movie/550?api_key=510e67617f4ffb3cd980b3075e2ce149
    // movie_id = 550 is for the movie Fight club

    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );



}
