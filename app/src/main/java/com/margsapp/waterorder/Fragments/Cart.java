package com.margsapp.waterorder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.margsapp.waterorder.Adapter.CartAdapter;
import com.margsapp.waterorder.Adapter.ProductAdapter;
import com.margsapp.waterorder.Model.Price;
import com.margsapp.waterorder.Model.Product;
import com.margsapp.waterorder.R;

import java.util.ArrayList;

public class Cart extends Fragment {


    ArrayList<com.margsapp.waterorder.Model.Cart> Cartlist;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;

    TextView total_price;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        total_price = view.findViewById(R.id.total_price);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Cartlist = new ArrayList<>();
        loadData();


        return view;
    }

    private void loadData() {

        Cartlist.clear();


        Query query = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Cartlist.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    com.margsapp.waterorder.Model.Cart cart = snapshot1.getValue(com.margsapp.waterorder.Model.Cart.class);

                    Cartlist.add(cart);

                }

                cartAdapter = new CartAdapter(getContext(), Cartlist,firebaseUser);
                recyclerView.setAdapter(cartAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("CartValue");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Price price = snapshot.getValue(Price.class);

                    assert price != null;
                    if(snapshot.exists()){
                        total_price.setText(String.valueOf(price.getTotalPrice()));
                    }else {
                        total_price.setText(0);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}