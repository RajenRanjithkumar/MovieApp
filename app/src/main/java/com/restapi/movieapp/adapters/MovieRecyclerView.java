package com.restapi.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.restapi.movieapp.R;
import com.restapi.movieapp.models.MovieModel;
import com.restapi.movieapp.utils.Credentials;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static final int DISPLAY_SEARCH = 0;
    private static final int DISPLAY_POP = 1;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = null;

        if (viewType == DISPLAY_SEARCH)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            return new MovieViewHolder(view, onMovieListener);
        }
        else
            {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_item, parent, false);
                return new Popular_View_Holder(view, onMovieListener);
            }


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int i) {


        int itemViewType = getItemViewType(i);

        if(itemViewType == DISPLAY_SEARCH)
        {
            ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());
            ((MovieViewHolder)holder).release_date.setText(mMovies.get(i).getRelease_date());
            ((MovieViewHolder)holder).duration.setText(mMovies.get(i).getOriginal_language()    );

            ((MovieViewHolder)holder).ratingBar.setRating(mMovies.get(i).getVote_average()/2);

            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"+mMovies.get(i).getPoster_path())
                    .into(((MovieViewHolder)holder).imageView);
        }else {

            ((Popular_View_Holder)holder).title2.setText(mMovies.get(i).getTitle());
            ((Popular_View_Holder)holder).release_date2.setText(mMovies.get(i).getRelease_date());
            ((Popular_View_Holder)holder).duration2.setText(mMovies.get(i).getOriginal_language()    );

            ((Popular_View_Holder)holder).ratingBar2.setRating(mMovies.get(i).getVote_average()/2);

            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"+mMovies.get(i).getPoster_path())
                    .into(((Popular_View_Holder)holder).imageView2);
        }






    }

    @Override
    public int getItemCount() {

        if (mMovies != null)
        {
            return mMovies.size();
        }else
            return 0;

    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }


    //Getting the id of the movie clicked
    public MovieModel getSelectedMovie(int position) {

        if (mMovies != null){
            if (mMovies.size()>0)
            {
                return mMovies.get(position);
            }
        }
        return null;

    }


    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR)
        {
            return DISPLAY_SEARCH;
        }else
            return DISPLAY_POP;
    }
}
