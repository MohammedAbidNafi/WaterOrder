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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.waterorder.Fragments.Can;
import com.margsapp.waterorder.Fragments.Cart;
import com.margsapp.waterorder.Fragments.Favourites;
import com.margsapp.waterorder.Fragments.Home;
import com.margsapp.waterorder.Model.Order;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements PaymentResultWithDataListener {


    Toolbar toolbar;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    BottomNavigationView bottomNavigationView;

    TextView username,cityChange,selectedCity;

    FirebaseFirestore firestore;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Explore");

        drawerLayout = findViewById(R.id.drawerlayout);

        navigationView = findViewById(R.id.navigationView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        cityChange = findViewById(R.id.citySelectorTxt);
        selectedCity = findViewById(R.id.selectedCity);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        loadFragment(new Home());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        View navigationViewHeaderView = navigationView.getHeaderView(0);

        username = navigationViewHeaderView.findViewById(R.id.username);





        cityChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CitySelector.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {


                    case R.id.home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Home();
                        toolbar.setTitle("Explore");
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
                        toolbar.setTitle("My Cart");
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                        return true;


                    case R.id.your_order:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
                        return true;


                    case R.id.favourites:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        fragment = new Favourites();
                        loadFragment(fragment);
                        toolbar.setTitle("My Favourites");
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
                        startActivity(new Intent(MainActivity.this, LanguageActivity.class));
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
                switch (item.getItemId()) {

                    case R.id.home:
                        fragment = new Home();
                        loadFragment(fragment);
                        toolbar.setTitle("Explore");
                        navigationView.setCheckedItem(R.id.home);
                        return true;


                    case R.id.favourites:
                        fragment = new Favourites();
                        loadFragment(fragment);
                        toolbar.setTitle("My Favourites");
                        navigationView.setCheckedItem(R.id.favourites);
                        return true;


                    case R.id.can:
                        fragment = new Can();
                        loadFragment(fragment);
                        return true;


                    case R.id.cart:
                        fragment = new Cart();
                        loadFragment(fragment);
                        toolbar.setTitle("My Cart");
                        navigationView.setCheckedItem(R.id.cart);
                        return true;

                }
                return false;
            }
        });


        loadData();

    }

    private void loadData(){

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Username = document.getString("username");
                        String CityName = document.getString("City");

                        selectedCity.setText(String.format("%s%s%s", getApplicationContext().getString(R.string.SelectedCity)," ", CityName));
                        username.setText(Username);



                    }
                }
            }
        });

    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    public void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.home);
        loadData();
    }


    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {



        try{
            Intent intent = new Intent(MainActivity.this,PaymentSuccessActivity.class);
            intent.putExtra("orderID",paymentData.getOrderId());
            intent.putExtra("paymentID",paymentData.getPaymentId());
            intent.putExtra("s",s);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Payment success in Cart " + s, Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Something went wrong please try again later",Toast.LENGTH_SHORT).show();

            Log.e("Razorpay Error", e.getMessage());
        }




    }


    public void Main(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}