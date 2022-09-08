package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.margsapp.iosdialog.iOSDialog;
import com.margsapp.iosdialog.iOSDialogListener;
import com.margsapp.waterorder.Fragments.Can;
import com.margsapp.waterorder.Fragments.Cart;
import com.margsapp.waterorder.Fragments.Favourites;
import com.margsapp.waterorder.Fragments.Home;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    RadioButton english,tamil,telugu,hindi,kannada,malayalam;

    int languageid;

    Toolbar toolbar;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    AppCompatButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawerlayout);

        navigationView = findViewById(R.id.navigationView);

        back = findViewById(R.id.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        english = findViewById(R.id.english);
        tamil = findViewById(R.id.tamil);
        telugu = findViewById(R.id.telugu);
        hindi = findViewById(R.id.hindi);
        kannada = findViewById(R.id.kannada);
        malayalam = findViewById(R.id.malayalam);

        loadData();
        SharedPreferences preferences = getSharedPreferences("lang_settings", Activity.MODE_PRIVATE);
        languageid = preferences.getInt("langid", 0);


        if(languageid == 0){
            english.setChecked(true);
        }
        if(languageid == 1){
            tamil.setChecked(true);
        }
        if(languageid == 2){
            telugu.setChecked(true);
        }
        if(languageid == 3){
            hindi.setChecked(true);
        }
        if(languageid == 4){
            kannada.setChecked(true);
        }
        if(languageid == 5){
            malayalam.setChecked(true);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }

    private void loadData() {
        SharedPreferences preferences = getSharedPreferences("lang_settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("lang","");
        int langid = preferences.getInt("langid", 0);
        setLocale(language, langid);
    }


    public void onLanguageChange(View view){
        switch (view.getId()){

            case R.id.english:
                if (english.isChecked()){
                   setLocale("en", 0);
                   startActivity(new Intent(LanguageActivity.this,MainActivity.class));

                }
                break;

            case R.id.tamil:
                if (tamil.isChecked()){
                    setLocale("ta",1);
                    startActivity(new Intent(LanguageActivity.this,MainActivity.class));
                }
                break;

            case R.id.telugu:
                if (telugu.isChecked()){

                    setLocale("te",2);
                    startActivity(new Intent(LanguageActivity.this,MainActivity.class));

                }
                break;

            case R.id.hindi:
                if (hindi.isChecked()){

                    setLocale("hi",3);
                    startActivity(new Intent(LanguageActivity.this,MainActivity.class));

                }
                break;

            case R.id.kannada:
                if (kannada.isChecked()){

                   setLocale("kn",4);
                   startActivity(new Intent(LanguageActivity.this,MainActivity.class));

                }
                break;

            case R.id.malayalam:
                if (malayalam.isChecked()){

                   setLocale("ml",5);
                   startActivity(new Intent(LanguageActivity.this,MainActivity.class));

                }
                break;



        }
    }

    private void setLocale(String lang, int langid) {


        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("lang_settings",MODE_PRIVATE).edit();
        editor.putString("lang",lang);
        editor.putInt("langid",langid);
        editor.apply();

    }

}