package com.example.mymoviecatalogue;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class LangugeActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languge);

        switch (getResources().getConfiguration().locale.getLanguage()) {
            case "en":
                radioButton = findViewById(R.id.rb_en);
                radioButton.setChecked(true);
                break;

            case "in":
                radioButton = findViewById(R.id.rb_in);
                radioButton.setChecked(true);
                break;
        }

        radioGroup = findViewById(R.id.rg_languge);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                Locale myLocale;
                Intent refresh = new Intent(LangugeActivity.this, MainActivity.class);

                switch (checkedId) {
                    case R.id.rb_en:
                        myLocale = new Locale("en");
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        finish();
                        startActivity(refresh);
                        break;

                    case R.id.rb_in:
                        myLocale = new Locale("in");
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);
                        finish();
                        startActivity(refresh);
                        break;
                }
            }
        });
    }
}
