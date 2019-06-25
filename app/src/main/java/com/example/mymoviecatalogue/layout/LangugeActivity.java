package com.example.mymoviecatalogue.layout;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.model.MovieResults;
import com.example.mymoviecatalogue.notification.MovieDailyReceiver;
import com.example.mymoviecatalogue.notification.MovieUpcomingReceiver;
import com.example.mymoviecatalogue.pref.SettingPreference;
import com.example.mymoviecatalogue.presenter.CheckLanguage;
import com.example.mymoviecatalogue.presenter.ClientAPI;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mymoviecatalogue.config.Config.API_KEY;

public class LangugeActivity extends AppCompatActivity {

    private MovieDailyReceiver movieDailyReceiver;
    private MovieUpcomingReceiver movieUpcomingReceiver;
    private SettingPreference settingPreference;

    private Switch dailySwitch, todaySwitch;
    private RadioButton rbEng, rbInd;

    private String language;

    private static final String ID = "id-ID";
    private static final String US = "en-US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languge);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.setting);

        RadioGroup radioGroup = findViewById(R.id.rg_languge);

        dailySwitch = findViewById(R.id.daily_switch);
        todaySwitch = findViewById(R.id.today_switch);
        rbEng = findViewById(R.id.rb_en);
        rbInd = findViewById(R.id.rb_in);

        movieDailyReceiver = new MovieDailyReceiver();
        movieUpcomingReceiver = new MovieUpcomingReceiver();
        settingPreference = new SettingPreference(this);

        checkLanguage();
        checkDailyReminder();
        checkTodayReminder();

        language = CheckLanguage.getLanguage(getBaseContext());

        dailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    movieDailyReceiver.setAlarm(getBaseContext());
                    settingPreference.setPrefDailyReminder(true);
                    Toast.makeText(LangugeActivity.this, getResources().getString(R.string.daily_reminder_active), Toast.LENGTH_SHORT).show();
                } else {
                    movieDailyReceiver.cancelAlarm(getBaseContext());
                    settingPreference.setPrefDailyReminder(false);
                    Toast.makeText(LangugeActivity.this, getResources().getString(R.string.daily_reminder_deactive), Toast.LENGTH_SHORT).show();
                }
            }
        });

        todaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setReleaseAlarm();
                    settingPreference.setPrefTodayReminder(true);
                    Toast.makeText(LangugeActivity.this, getResources().getString(R.string.today_reminder_active), Toast.LENGTH_SHORT).show();
                } else {
                    movieUpcomingReceiver.cancelAlarm(getBaseContext());
                    settingPreference.setPrefTodayReminder(false);
                    Toast.makeText(LangugeActivity.this, getResources().getString(R.string.today_reminder_deactive), Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                Locale myLocale;
                Intent refresh = new Intent(LangugeActivity.this, MainActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                switch (checkedId) {
                    case R.id.rb_en:
                        myLocale = new Locale("en");
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        settingPreference.setPrefLanguage(US);
                        break;

                    case R.id.rb_in:
                        myLocale = new Locale("in");
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        settingPreference.setPrefLanguage(ID);
                        break;
                }

                finish();
                startActivity(refresh);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void checkLanguage() {
        if (CheckLanguage.getLanguage(this).equals(US)) {
            rbEng.setChecked(true);
        } else if (CheckLanguage.getLanguage(this).equals(ID)) {
            rbInd.setChecked(true);
        }
    }

    private void checkDailyReminder() {
        if (settingPreference.getPrefDailyReminder()) {
            dailySwitch.setChecked(true);
        } else {
            dailySwitch.setChecked(false);
        }
    }

    private void checkTodayReminder() {
        if (settingPreference.getPrefTodayReminder()) {
            todaySwitch.setChecked(true);
        } else {
            todaySwitch.setChecked(false);
        }
    }

    private void setReleaseAlarm() {

        ClientAPI.getUpcoming service = ClientAPI
                .getClient()
                .create(ClientAPI.getUpcoming.class);

        Call<MovieResults> call = service.getMovie(API_KEY, language, "1");
        call.enqueue(new Callback<MovieResults>() {

            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {

                if (response.body() != null) {
                    movieUpcomingReceiver.setAlarm(getBaseContext(), response.body().getResults());
                }

            }

            @Override
            public void onFailure(@NonNull Call<MovieResults> call, @NonNull Throwable t) {

            }
        });

    }
}
