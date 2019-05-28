package com.example.mymoviecatalogue.layout;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.pref.SettingPreference;
import com.example.mymoviecatalogue.presenter.CheckLanguage;

import java.util.Locale;

public class LangugeActivity extends AppCompatActivity {

    private SettingPreference settingPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languge);

        RadioGroup radioGroup = findViewById(R.id.rg_languge);
        settingPreference = new SettingPreference(this);
        RadioButton radioButton;

        if (CheckLanguage.getLanguage(this).equals("en-US")) {
            radioButton = findViewById(R.id.rb_en);
            radioButton.setChecked(true);
        } else if (CheckLanguage.getLanguage(this).equals("id-ID")) {
            radioButton = findViewById(R.id.rb_in);
            radioButton.setChecked(true);
        }

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
                        settingPreference.setPrefLanguage("en-US");
                        break;

                    case R.id.rb_in:
                        myLocale = new Locale("in");
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        settingPreference.setPrefLanguage("id-ID");
                        break;
                }

//                MainActivity.mainActivity.finish();
                finish();
                startActivity(refresh);
            }
        });
    }
}
