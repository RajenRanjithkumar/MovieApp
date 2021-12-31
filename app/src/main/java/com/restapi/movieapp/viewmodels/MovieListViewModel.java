package com.restapi.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.restapi.movieapp.models.MovieModel;
import com.restapi.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    //this class is used for viewmodel


    private  MovieRepository movieRepository;

    //constructor
    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies()
    {
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPop()
    {
        return movieRepository.getPop();
    }


    // 3-Calling method in view model
    public void searchMovieApi(String query, int pageNumber)
    {
        movieRepository.serachMovieApi(query, pageNumber);
    }

    // 3-Calling method in view model for popular movies
    public void searchMoviePop(int pageNumber)
    {
        movieRepository.serachMoviePopular(pageNumber);
    }

    public void searchNextpage(){
        movieRepository.searchNextPage();
    }

}
