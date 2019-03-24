package com.example.mymoviecatalogue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class MainPresenter {

//    private MainView mainView;
//
//    MainPresenter(MainView mainView) {
//        this.mainView = mainView;
//    }

    public static class MovieAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<MainModel.Movie> movies;

        void setMovies(ArrayList<MainModel.Movie> movies) {
            this.movies = movies;
        }

        MovieAdapter(Context context) {
            this.context = context;
            movies = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return movies.size();
        }

        @Override
        public Object getItem(int i) {
            return movies.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.movie_list, viewGroup, false);
            }

            ViewHolder viewHolder = new ViewHolder(view);
            MainModel.Movie model = (MainModel.Movie) getItem(i);
            viewHolder.bind(model);
            return view;
        }

        class ViewHolder {
            private TextView txtTitle;
            private TextView txtDescription;
            private ImageView imgPhoto;

            ViewHolder(View view) {
                txtTitle = view.findViewById(R.id.txt_title);
                txtDescription = view.findViewById(R.id.txt_short_des);
                imgPhoto = view.findViewById(R.id.img_photo);
            }

            void bind(MainModel.Movie movie) {
                txtTitle.setText(movie.getTitle());
                txtDescription.setText(movie.getDescription());
                Glide.with(context).load(movie.getPhoto()).into(imgPhoto);
            }
        }
    }
}
