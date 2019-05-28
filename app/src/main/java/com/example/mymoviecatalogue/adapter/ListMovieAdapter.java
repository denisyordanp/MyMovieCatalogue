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

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> movies;

    public ListMovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvDescription;
        ImageView imgPhoto;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.txt_title);
            tvDescription = itemView.findViewById(R.id.txt_short_des);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }

    @NonNull
    @Override
    public ListMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(movieRow);

    }

    @Override
    public void onBindViewHolder(@NonNull ListMovieAdapter.MovieViewHolder holder, int position) {

        if (movies.get(position).getTitle() != null) {
            holder.tvJudul.setText(movies.get(position).getTitle());
        } else {
            holder.tvJudul.setText(movies.get(position).getName());
        }

        if (!movies.get(position).getDescription().isEmpty()){
            holder.tvDescription.setText(movies.get(position).getDescription());
        }else {
            holder.tvDescription.setText(context.getResources().getString(R.string.not_available));

        }

        CircularProgressDrawable progress = new CircularProgressDrawable(context);
        progress.setStrokeWidth(5f);
        progress.setCenterRadius(30f);
        progress.start();

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(MainActivity.BASE_URL + movies.get(position).getPoster())
                .placeholder(progress)
                .error(R.drawable.ic_error_black_24dp)
                .fit()
                .centerCrop()
                .into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}