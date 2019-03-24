package com.example.mymoviecatalogue;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter.MovieAdapter adapter;
    private ArrayList<MainModel.Movie> movies;
    private String[] dataTitle, dataDescription, dataDirectors, dataRelease;
    private TypedArray dataPhoto;
    ListView listView;
    TextView txtTitle, txtDescription;
    ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MainPresenter.MovieAdapter(this);
        listView = findViewById(R.id.lv_list);
        listView.setAdapter(adapter);

        txtTitle = findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_short_des);
        imgPhoto = findViewById(R.id.img_photo);

        prepare();
        addItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MainModel.Movie movie = new MainModel.Movie();
                movie.setTitle(movies.get(i).getTitle());
                movie.setDescription(movies.get(i).getDescription());
                movie.setDirectors(movies.get(i).getDirectors());
                movie.setRelease(movies.get(i).getRelease());
                movie.setPhoto(movies.get(i).getPhoto());

                Intent intent = new Intent(MainActivity.this, MovieDetail.class);
                intent.putExtra(MovieDetail.EXTRA_MOVIE, movie);
                startActivity(intent);
            }
        });

    }

    private void addItem() {
        movies = new ArrayList<>();

        for (int i = 0; i < dataTitle.length; i++) {
            MainModel.Movie movie = new MainModel.Movie();
            movie.setPhoto(dataPhoto.getResourceId(i, -1));
            movie.setTitle(dataTitle[i]);
            movie.setDescription(dataDescription[i]);
            movie.setDirectors(dataDirectors[i]);
            movie.setRelease(dataRelease[i]);
            movies.add(movie);
        }
        adapter.setMovies(movies);
    }

    private void prepare() {
        dataTitle = getResources().getStringArray(R.array.data_title);
        dataPhoto = getResources().obtainTypedArray(R.array.data_photo);
        dataDescription = getResources().getStringArray(R.array.data_description);
        dataDirectors = getResources().getStringArray(R.array.data_directors);
        dataRelease = getResources().getStringArray(R.array.data_release);
    }

    @Override
    public void showMovie(MainModel.Movie modelMovie) {
    }
}
