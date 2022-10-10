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
        navigationView.setCheckedItem(R.id.language);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){


                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(LanguageActivity.this,MainActivity.class));
                        return true;

                    /*

                    case R.id.can:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Can();
                        loadFragment(fragment);
                        bottomNavigationView.setSelectedItemId(R.id.can);
                        return true;

                     */

                    case R.id.cart:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(LanguageActivity.this,CartActivity.class));
                        return true;

                        /*
                    case R.id.your_order:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, YourOrder.class));
                        return true;
                        */

                    /*
                    case R.id.favourites:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Favourites();
                        loadFragment(fragment);
                        bottomNavigationView.setSelectedItemId(R.id.favourites);
                        return true;

                     */


                    /*
                    case R.id.account:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, YourAccount.class));
                        return true;


                         */
                    case R.id.language:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        navigationView.setCheckedItem(R.id.language);

                        return true;
                    /*
                    case R.id.settings:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this,Settings.class));

                    case R.id.customercare:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this,CustomerCare.class));
                        return true;

                     */





                    case R.id.logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(LanguageActivity.this, StartActivity.class));
                        return true;





                }
                return false;
            }
        });

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


    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.language);
    }



}