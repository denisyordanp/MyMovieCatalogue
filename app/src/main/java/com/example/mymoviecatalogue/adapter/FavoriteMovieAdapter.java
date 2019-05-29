package com.example.mymoviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.mymoviecatalogue.layout.MovieDetailActivity;
import com.example.mymoviecatalogue.model.MovieFavorite;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder>{

    private Context context;
    private ArrayList<MovieFavorite> aMovies = new ArrayList<>();

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<MovieFavorite> movie){
        aMovies.clear();
        aMovies.addAll(movie);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvDescription;
        ImageView imgPhoto;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.txt_title);
            tvDescription = itemView.findViewById(R.id.txt_short_des);
            imgPhoto = itemView.findViewById(R.id.img_photo);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){

                        MovieFavorite clickedDataItem = aMovies.get(pos);
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailActivity.EXTRA_FAVORITE, clickedDataItem);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(movieRow);

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {

        holder.tvJudul.setText(aMovies.get(position).getTitle());

        if (!aMovies.get(position).getOverview().isEmpty()){
            holder.tvDescription.setText(aMovies.get(position).getOverview());
        }else {
            holder.tvDescription.setText(context.getResources().getString(R.string.not_available));
        }

        CircularProgressDrawable progress = new CircularProgressDrawable(context);
        progress.setStrokeWidth(5f);
        progress.setCenterRadius(30f);
        progress.start();

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(MainActivity.BASE_URL + aMovies.get(position).getPosterPath())
                .placeholder(progress)
                .error(R.drawable.ic_error_black_24dp)
                .fit()
                .centerCrop()
                .into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return aMovies.size();
    }

}
