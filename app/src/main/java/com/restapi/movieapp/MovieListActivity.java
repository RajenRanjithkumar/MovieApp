 package com.restapi.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.restapi.movieapp.adapters.MovieRecyclerView;
import com.restapi.movieapp.adapters.OnMovieListener;
import com.restapi.movieapp.models.MovieModel;
import com.restapi.movieapp.request.Servicey;
import com.restapi.movieapp.response.MovieSearchResponse;
import com.restapi.movieapp.utils.Credentials;
import com.restapi.movieapp.utils.MovieApi;
import com.restapi.movieapp.viewmodels.MovieListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    //Before we run our app, we need to add the network security Config


    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SetupSearchView();

        recyclerView = findViewById(R.id.recyclerView);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();

        ObservePopularMovies();
        //Getting data for popular
        movieListViewModel.searchMoviePop(1);

        //to test the api
        //searchMovieApi("fast",1);
        // test the method
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovieApi("fast",2);

            }
        });*/


    }

    private void ObservePopularMovies() {

        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {

                //observing for any data change

                if (movieModels != null)
                {
                    //to change the item view design
                    Credentials.POPULAR = false;
                    //
                    for(MovieModel movieModel: movieModels)
                    {
                        //get the data in the log
                        Log.v("Tag", "OnChangeg: "+movieModel.getTitle());

                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }


    //observing any data change
    private void ObserveAnyChange()
    {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {

                //observing for any data change

                if (movieModels != null)
                {
                    //to change the item view design
                    Credentials.POPULAR=true;
                    //
                    for(MovieModel movieModel: movieModels)
                    {
                        //get the data in the log
                        Log.v("Tag", "OnChangeg: "+movieModel.getTitle());

                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    // 4- Calling method in main activity
    /*private void searchMovieApi(String query, int pageNumber)
    {
        movieListViewModel.searchMovieApi(query, pageNumber);
    }*/



    private void GetRetrofitResponse() {

        //error check
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(Credentials.API_KEY,
                        "Jack Reacher",
                        1);

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {

                if(response.code() == 200){

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    //Log.v("Tag", "Res body"+ response.body());


                    for(MovieModel movie: movies)
                    {
                        Log.v("Tag", "Release date  "+ movie.getRelease_date());
                    }
                }
                else {

                try {
                    Log.v("Tag", "Error"+response.errorBody().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });

    }

    private void GetRetrofitResponseAccordingToID(){

        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(550, Credentials.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                if(response.code() == 200)
                {
                    MovieModel movie = response.body();
                    Log.v("Tag", "Response msg "+ movie.getTitle());
                }
                else
                    {
                        try {
                            Log.v("Tag", "Error"+response.errorBody().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }


    // 5- Initializing recyclerView & adding data to it
    private void ConfigureRecyclerView(){

        movieRecyclerViewAdapter = new MovieRecyclerView(this);

        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //recycler view pagination
        //Loading Next page of API response

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){

                    // Here we need to display the next page of the search result
                    movieListViewModel.searchNextpage();

                }

            }
        });


    }

    @Override
    public void onMovieClick(int position) {

        //Toast.makeText(this,"Position no: "+position, Toast.LENGTH_SHORT).show();

        // we don't need the position of the movie from recyclerView
        //we need the movie ID to get all the details about it

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);


    }

    @Override
    public void onCategoryClick(String category) {

    }


    //get data from searchview and query the api to get the results(Movies)
    private void SetupSearchView() {

        final SearchView searchView = findViewById(R.id.search_view);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                movieListViewModel.searchMovieApi(
                        //string from search view
                        query,
                        1
                );
                if(query.isEmpty())
                {
                    ObservePopularMovies();
                    //to change the item view design
                    Credentials.POPULAR=false;
                    //
                }

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                movieListViewModel.searchMovieApi(
                        //string from search view
                        newText,
                        1
                );
                if(newText.isEmpty())
                {
                    ObservePopularMovies();
                    //to change the item view design
                    Credentials.POPULAR=false;
                    //
                }

                return false;
            }
        });


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });
    }








}