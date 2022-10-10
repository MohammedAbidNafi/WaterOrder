package com.margsapp.waterorder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        SharedPreferences sharedPreferences = getSharedPreferences("lang_settings", Activity.MODE_PRIVATE);
        String language = sharedPreferences.getString("lang", "");
        setLocale(language);

        new Handler().postDelayed(() -> {

            Intent i = new Intent(SplashActivity.this, StartActivity.class);

            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            // close this activity
            finish();

        }, 3 * 1000); // wait for 3 seconds



    }

    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
}