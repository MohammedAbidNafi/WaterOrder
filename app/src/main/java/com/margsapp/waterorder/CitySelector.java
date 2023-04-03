package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.margsapp.waterorder.Adapter.CitiesAdapter;
import com.margsapp.waterorder.Adapter.ProductAdapter;
import com.margsapp.waterorder.Model.Cities;
import com.margsapp.waterorder.Model.Product;

import java.util.ArrayList;
import java.util.Objects;

public class CitySelector extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Cities> citiesArrayList;

    CitiesAdapter citiesAdapter;

    FirebaseUser firebaseUser;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selector);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager= new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        citiesArrayList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        loadCities();
    }

    private void loadCities() {

        Query query = FirebaseDatabase.getInstance().getReference("Location");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Cities cities = snapshot1.getValue(Cities.class);



                    citiesArrayList.add(cities);



                }

                citiesAdapter = new CitiesAdapter(CitySelector.this, citiesArrayList,firebaseUser);
                recyclerView.setAdapter(citiesAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}