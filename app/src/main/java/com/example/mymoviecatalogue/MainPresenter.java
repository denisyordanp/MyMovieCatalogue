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

class MainPresenter {

//    private MainView mainView;
//
//    MainPresenter(MainView mainView) {
//        this.mainView = mainView;
//    }

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
            return new MainPresenter.ListMovieAdapter.MovieViewHolder(movieRow);

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

//
//        @Override
//        public int getCount() {
//            return movies.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return movies.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            if (view == null) {
//                view = LayoutInflater.from(context).inflate(R.layout.movie_list, viewGroup, false);
//            }
//
//            ViewHolder viewHolder = new ViewHolder(view);
//            MainModel.Movie model = (MainModel.Movie) getItem(i);
//            viewHolder.bind(model);
//            return view;
//        }
//
//        class ViewHolder {
//            private TextView txtTitle;
//            private TextView txtDescription;
//            private ImageView imgPhoto;
//
//            ViewHolder(View view) {
//                txtTitle = view.findViewById(R.id.txt_title);
//                txtDescription = view.findViewById(R.id.txt_short_des);
//                imgPhoto = view.findViewById(R.id.img_photo);
//            }
//
//            void bind(MainModel.Movie movie) {
//                txtTitle.setText(movie.getTitle());
//                txtDescription.setText(movie.getDescription());
//                Glide.with(context).load(movie.getPhoto()).into(imgPhoto);
//            }
//        }

    }
}
