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
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.layout.MovieDetailActivity;
import com.example.mymoviecatalogue.model.Movie;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.mymoviecatalogue.config.Config.NOTIFICATION_CHANNEL_ID;
import static com.example.mymoviecatalogue.config.Config.NOTIFICATION_CHANNEL_NAME;
import static com.example.mymoviecatalogue.config.Config.NOTIFICATION_ID;
import static com.example.mymoviecatalogue.layout.MovieDetailActivity.EXTRA_MOVIE;

public class MovieUpcomingReceiver extends BroadcastReceiver {

    private static int notifId = 1000;

    private static final String TITLE = "title";
    private static final String M_ID = "movie_id";
    private static final String POSTER = "poster";
    private static final String DATE = "date";
    private static final String RATING = "rating";
    private static final String OVERVIEW = "overview";

    @Override
    public void onReceive(Context context, Intent intent) {

        String movieTitle = intent.getStringExtra(TITLE);
        int movieId = intent.getIntExtra(M_ID, 0);
        String poster = intent.getStringExtra(POSTER);
        String date = intent.getStringExtra(DATE);
        String rate = intent.getStringExtra(RATING);
        String ovr = intent.getStringExtra(OVERVIEW);

        Movie movie = new Movie(poster, movieId, movieTitle, ovr, date, rate, null, null);
        String desc = context.getResources().getString(R.string.message_today) + movieTitle;
        sendNotification(context, context.getString(R.string.app_name), desc, notifId, movie);

    }

    private void sendNotification(Context context, String title, String desc, int id, Movie movie) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
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

    public void setAlarm(Context context, ArrayList<Movie> movieResults) {
        int delay = 0;

        for (Movie movie : movieResults) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, MovieUpcomingReceiver.class);
            intent.putExtra(TITLE, movie.getTitle());
            intent.putExtra(M_ID, movie.getId());
            intent.putExtra(POSTER, movie.getPoster());
            intent.putExtra(DATE, movie.getRelease());
            intent.putExtra(RATING, movie.getVote());
            intent.putExtra(OVERVIEW, movie.getDescription());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + delay, AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            notifId += 1;
            delay += 3000;
            Log.v("title", movie.getTitle());
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MovieUpcomingReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
