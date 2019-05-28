package com.example.mymoviecatalogue.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.model.Movie;

import java.util.List;

public class DatabaseFunction {

    private Context context;

    public DatabaseFunction(Context context) {
        this.context = context;
    }

    public void checkFavorite(final int id, final Menu menu){

        class Check extends AsyncTask<Void, Void, List<EntityMovie>> {

            @Override
            protected List<EntityMovie> doInBackground(Void... voids) {

                List<EntityMovie> result = DatabaseMovie
                        .getInstance(context.getApplicationContext())
                        .getAppDatabase()
                        .movieDao()
                        .check(id);

                return result;
            }

            @Override
            protected void onPostExecute(List<EntityMovie> lists) {
                super.onPostExecute(lists);

                if (!lists.isEmpty()){
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_white_full_24dp));
                    Log.d("fav", "Favorite");
                }

            }
        }

        Check ck = new Check();
        ck.execute();

    }

    public void deleteFavorite(final EntityMovie entityMovie){

        class DeleteFavorite extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseMovie.getInstance(context.getApplicationContext())
                        .getAppDatabase()
                        .movieDao()
                        .delete(entityMovie);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(context, context.getResources().getString(R.string.unfavorite), Toast.LENGTH_SHORT).show();
            }
        }

        DeleteFavorite dt = new DeleteFavorite();
        dt.execute();
    }

    public void saveFavorite(final Movie movie, final boolean isMovie){

        class SaveFavorite extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                EntityMovie entityMovie = new EntityMovie();
                entityMovie.setMovieid(movie.getId());
                entityMovie.setCategory(isMovie);

                DatabaseMovie.getInstance(context.getApplicationContext())
                        .getAppDatabase()
                        .movieDao()
                        .insert(entityMovie);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(context, context.getResources().getString(R.string.favorite), Toast.LENGTH_SHORT).show();

            }

        }

        SaveFavorite sf = new SaveFavorite();
        sf.execute();

    }
}
