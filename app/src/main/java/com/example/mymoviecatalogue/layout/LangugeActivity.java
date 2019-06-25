package com.example.mymoviecatalogue.layout;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.notification.MovieDailyReceiver;
import com.example.mymoviecatalogue.notification.MovieUpcomingReceiver;
import com.example.mymoviecatalogue.pref.SettingPreference;
import com.example.mymoviecatalogue.presenter.CheckLanguage;

import java.util.Locale;
import java.util.Objects;

public class LangugeActivity extends AppCompatActivity {

    private MovieDailyReceiver movieDailyReceiver;
    private MovieUpcomingReceiver movieUpcomingReceiver;
    private SettingPreference settingPreference;

    private Switch dailySwitch, todaySwitch;
    private RadioButton rbEng, rbInd;

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
                    movieUpcomingReceiver.setAlarm(getBaseContext());
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

}
