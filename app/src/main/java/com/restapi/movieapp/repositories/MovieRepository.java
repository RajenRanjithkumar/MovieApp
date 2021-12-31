package com.restapi.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.restapi.movieapp.models.MovieModel;
import com.restapi.movieapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {


    private static MovieRepository instance;
    private MovieApiClient movieApiClient;

    // to display the results from next page
    private String mQuery;
    private int mPageNumber;


    public static MovieRepository getInstance()
    {
        if( instance == null)
        {
            instance = new MovieRepository();
        }

        return instance;
    }

    private MovieRepository()
    {
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies()
    {
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPop()
    {
        return movieApiClient.getMoviesPop();
    }

    //2-Calling the method in repository

    public void serachMovieApi(String query, int pageNumber)
    {

        mQuery = query;
        mPageNumber = pageNumber;

        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void serachMoviePopular(int pageNumber)
    {


        mPageNumber = pageNumber;

        movieApiClient.searchMoviesPop( pageNumber);
    }


    public void searchNextPage(){

        serachMovieApi(mQuery,mPageNumber+1);
    }


}
