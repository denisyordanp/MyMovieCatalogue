package com.example.mymoviecatalogue.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.layout.MovieDetailActivity;
import com.example.mymoviecatalogue.model.Movie;
import com.example.mymoviecatalogue.model.MovieResults;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.ClientAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mymoviecatalogue.BuildConfig.API_KEY;
import static com.example.mymoviecatalogue.config.Config.NOTIFICATION_CHANNEL_ID;
import static com.example.mymoviecatalogue.config.Config.NOTIFICATION_CHANNEL_NAME;
import static com.example.mymoviecatalogue.config.Config.NOTIFICATION_ID;
import static com.example.mymoviecatalogue.layout.MovieDetailActivity.EXTRA_FAVORITE;

public class MovieUpcomingReceiver extends BroadcastReceiver {

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MovieUpcomingReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        String language = CheckLanguage.getLanguage(context);

        ClientAPI.getUpcoming service = ClientAPI
                .getClient()
                .create(ClientAPI.getUpcoming.class);

        Call<MovieResults> call = service.getMovie(API_KEY, language, "1");
        call.enqueue(new Callback<MovieResults>() {

            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {

                if (response.body() != null) {

                    ArrayList<Movie> movie = response.body().getResults();

                    for (int i = 0; i < movie.size(); i++) {
                        String today = movie.get(i).getRelease();
                        if (today.equals(getCurrentDate())) {
                            String desc = context.getResources().getString(R.string.message_today) + movie.get(i).getTitle();
                            int notifId = 1000;
                            sendNotification(context, context.getString(R.string.app_name), desc, notifId, movie.get(i));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResults> call, @NonNull Throwable t) {
                Log.d("Reminder daily", "onFailure");
            }
        });
    }

    private void sendNotification(Context context, String title, String desc, int id, Movie movie) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_FAVORITE, movie);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(title)
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(uriTone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, getPendingIntent(context));
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

}
