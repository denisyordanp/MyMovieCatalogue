package com.example.mymoviecatalogue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<MainModel.Movie> movies;

    ListMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<MainModel.Movie> getMovies() {
        return movies;
    }

    void setMovies(ArrayList<MainModel.Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ListMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(movieRow);

    }

    @Override
    public void onBindViewHolder(@NonNull ListMovieAdapter.MovieViewHolder holder, int position) {

        holder.tvJudul.setText(getMovies().get(position).getTitle());
        holder.tvDescription.setText(getMovies().get(position).getDescription());
        Glide.with(context).load(getMovies().get(position).getPhoto()).apply(new RequestOptions().override(55, 55)).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return getMovies().size();
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
}