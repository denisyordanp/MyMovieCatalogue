package com.example.favoriteconsumerapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.favoriteconsumerapp.R;
import com.example.favoriteconsumerapp.database.DatabaseContract;
import com.example.favoriteconsumerapp.model.FavoriteEntity;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Cursor cursor;
    private Context context;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setData(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int i) {

        if (cursor.moveToPosition(i)) {
            holder.tvJudul.setText(DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.TITLE));
            holder.tvDescription.setText(DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.DATE));

            CircularProgressDrawable progress = new CircularProgressDrawable(context);
            progress.setStrokeWidth(5f);
            progress.setCenterRadius(30f);
            progress.start();

            String BASE_IMG_URL = "https://image.tmdb.org/t/p/w500";

            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(BASE_IMG_URL + DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.POSTER))
                    .placeholder(progress)
                    .fit()
                    .centerCrop()
                    .into(holder.imgPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul, tvDescription;
        ImageView imgPhoto;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.txt_title);
            tvDescription = itemView.findViewById(R.id.txt_short_des);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }
}
