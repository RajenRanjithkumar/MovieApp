package com.restapi.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.restapi.movieapp.AppExecutors;
import com.restapi.movieapp.models.MovieModel;
import com.restapi.movieapp.response.MovieSearchResponse;
import com.restapi.movieapp.utils.Credentials;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    //Live data for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;
    // making Global RUNNABLE
    private RetrieveMoviesRunnable retrieveMoviesRunnable;


    //Live data for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPopular;
    // making popular RUNNABLE
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;

    public static MovieApiClient getInstance()
    {
        if (instance == null)
        {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient()
    {
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
    }



    public LiveData<List<MovieModel>> getMovies()
    {
        return mMovies;
    }

    public LiveData<List<MovieModel>> getMoviesPop()
    {
        return mMoviesPopular;
    }





    // 1-This method that we are going to call through the classes
    public void searchMoviesApi(String query, int pageNumber)
    {

        if(retrieveMoviesRunnable != null)
        {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().NetworkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().NetworkIO().schedule(new Runnable() {
            @Override
            public void run() {

                // Cancelling the retrofit call
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    public void searchMoviesPop( int pageNumber)
    {

        if(retrieveMoviesRunnablePop != null)
        {
            retrieveMoviesRunnablePop = null;
        }

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().NetworkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().NetworkIO().schedule(new Runnable() {
            @Override
            public void run() {

                // Cancelling the retrofit call
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);

    }


    //Retrieve data from RestApi by runnable class
    //we have 2 types of queries: the ID & searchQueries

    private class RetrieveMoviesRunnable implements Runnable{


        private String query;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        private int pageNumber;
        boolean cancelRequest;

        @Override
        public void run() {

            //getting the response objects
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest){
                    return;
                }

                if (response.code() == 200){


                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    //if (pageNumber == 1) changed to
                    if (pageNumber != 0)
                    {
                        mMovies.postValue(list);
                    }else
                        {
                            List<MovieModel> currentMovies = mMovies.getValue();
                            currentMovies.addAll(list);
                            mMovies.postValue(currentMovies);
                        }

                }
                else {

                    String error = response.errorBody().toString();
                    Log.v("Tag","Error msg"+error);
                    mMovies.postValue(null);

                }

            } catch (Exception e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }




        }

        //search Method/ query



        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
                    );
        }
        private void CancelRequest(){

            Log.v("TAG", " Cancelling Request");
            cancelRequest = true;
        }


    }

    //Retrieve data from RestApi by runnable class for popular movies


    private class RetrieveMoviesRunnablePop implements Runnable{


        private String query;

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        private int pageNumber;
        boolean cancelRequest;

        @Override
        public void run() {

            //getting the response objects
            try {
                Response response2 = getMoviesPop(pageNumber).execute();
                if (cancelRequest){
                    return;
                }

                if (response2.code() == 200){


                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    //if (pageNumber == 1) changed to
                    if (pageNumber != 0)
                    {
                        mMoviesPopular.postValue(list);
                    }else
                    {
                        List<MovieModel> currentMovies = mMoviesPopular.getValue();
                        currentMovies.addAll(list);
                        mMoviesPopular.postValue(currentMovies);
                    }

                }
                else {

                    String error = response2.errorBody().toString();
                    Log.v("Tag","Error msg"+error);
                    mMoviesPopular.postValue(null);

                }

            } catch (Exception e) {
                e.printStackTrace();
                mMoviesPopular.postValue(null);
            }




        }


        //search Method/ query

        private Call<MovieSearchResponse> getMoviesPop(int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void CancelRequest(){

            Log.v("TAG", " Cancelling Request");
            cancelRequest = true;
        }


    }



}
