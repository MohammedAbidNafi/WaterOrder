package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.margsapp.waterorder.Fragments.Can;
import com.margsapp.waterorder.Fragments.Cart;
import com.margsapp.waterorder.Fragments.Favourites;
import com.margsapp.waterorder.Fragments.Home;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawerlayout);

        navigationView = findViewById(R.id.navigationView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        loadFragment(new Home());



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){


                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Home();
                        loadFragment(fragment);
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        return true;

                    case R.id.can:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Can();
                        loadFragment(fragment);
                        bottomNavigationView.setSelectedItemId(R.id.can);
                        return true;

                    case R.id.cart:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Cart();
                        loadFragment(fragment);
                        bottomNavigationView.setSelectedItemId(R.id.cart);
                        return true;

                        /*
                    case R.id.your_order:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, YourOrder.class));
                        return true;
                        */

                    case R.id.favourites:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Favourites();
                        loadFragment(fragment);
                        bottomNavigationView.setSelectedItemId(R.id.favourites);
                        return true;

                        /*

                    case R.id.account:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, YourAccount.class));
                        return true;


                         */
                    case R.id.language:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this,LanguageActivity.class));
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
                        startActivity(new Intent(MainActivity.this, StartActivity.class));
                        return true;





                }
                return false;
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){

                    case R.id.home:
                        fragment = new Home();
                        loadFragment(fragment);
                        navigationView.setCheckedItem(R.id.home);
                        return true;


                    case R.id.favourites:
                        fragment = new Favourites();
                        loadFragment(fragment);
                        navigationView.setCheckedItem(R.id.favourites);
                        return true;


                    case R.id.can:
                        fragment = new Can();
                        loadFragment(fragment);
                        return true;


                    case R.id.cart:
                        fragment = new Cart();
                        loadFragment(fragment);
                        navigationView.setCheckedItem(R.id.cart);
                        return true;

                }
                return false;
            }
        });



    }


    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    public void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.home);
    }


    public void onBackPressed(){
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}