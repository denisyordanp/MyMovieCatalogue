package com.example.mymoviecatalogue.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.layout.MainActivity;
import com.example.mymoviecatalogue.model.Movie;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.mymoviecatalogue.config.Config.BASE_IMG_URL;

public class HorizontalListMovieAdapter extends RecyclerView.Adapter<HorizontalListMovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;

    public HorizontalListMovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView horizontalTvTitle;
        ImageView horizontalImgPhoto;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            horizontalTvTitle = itemView.findViewById(R.id.horizontal_rv_tv_title);
            horizontalImgPhoto = itemView.findViewById(R.id.horizontal_rv_poster);
        }
    }

    @NonNull
    @Override
    public HorizontalListMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_movie_list, parent, false);
        return new MovieViewHolder(movieRow);

    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalListMovieAdapter.MovieViewHolder holder, int position) {

        if (movies.get(position).getTitle() != null) {
            holder.horizontalTvTitle.setText(movies.get(position).getTitle());
        } else {
            holder.horizontalTvTitle.setText(movies.get(position).getName());
        }

        CircularProgressDrawable progress = new CircularProgressDrawable(context);
        progress.setStrokeWidth(5f);
        progress.setCenterRadius(30f);
        progress.start();

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(BASE_IMG_URL + movies.get(position).getPoster())
                .placeholder(progress)
                .error(R.drawable.ic_error_black_24dp)
                .fit()
                .centerCrop()
                .into(holder.horizontalImgPhoto);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}