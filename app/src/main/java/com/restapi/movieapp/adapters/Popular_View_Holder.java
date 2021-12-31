package com.restapi.movieapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restapi.movieapp.R;

import org.jetbrains.annotations.NotNull;

public class Popular_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title2, release_date2, duration2;
    ImageView imageView2;
    RatingBar ratingBar2;

    //click Listener
    OnMovieListener onMovieListener;


    public Popular_View_Holder(@NonNull @NotNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
        title2 = itemView.findViewById(R.id.movie_title_popular);
        release_date2 = itemView.findViewById(R.id.movie_category_popular);
        duration2 = itemView.findViewById(R.id.movie_duration_popular);
        imageView2 = itemView.findViewById(R.id.movie_image_view_popular);
        ratingBar2= itemView.findViewById(R.id.rating_bar_popular);

        //important
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        onMovieListener.onMovieClick(getAdapterPosition());

    }
}
