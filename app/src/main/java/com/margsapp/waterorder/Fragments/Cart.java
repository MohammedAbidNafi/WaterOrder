package com.margsapp.waterorder.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Cart extends Fragment {


    ArrayList<com.margsapp.waterorder.Model.Cart> Cartlist;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;

    TextView total_price;

    Checkout checkout;

    AppCompatButton checkout_btn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        total_price = view.findViewById(R.id.total_price);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        checkout_btn = view.findViewById(R.id.checkout);




        Checkout.preload(getContext());

        checkout = new Checkout();

        checkout.setKeyID("rzp_test_H2r4ZREd5wGlhu");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Cartlist = new ArrayList<>();
        loadData();

        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayement();
            }
        });


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


    public void startPayement(){
        Checkout checkout = new Checkout();

        String ID = Randomizer(6);

        checkout.setImage(R.mipmap.ic_launcher_round);
        final Activity activity = getActivity();


        try {


            JSONObject options = new JSONObject();

            options.put("name", "Water Order");
            options.put("description","Order water");
            options.put("theme.color", "#276583");
            options.put("currency","INR");
            options.put("amount",total_price.getText().toString()+"00");

            checkout.open(activity,options);
        }catch (Exception e){
            Log.e("Error with Razorpay","Error starting Razorpay", e);
        }
    }


    private String Randomizer(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890" + "abcdefghijklmnopqrstuvxyz";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string

        for(int i = 0; i < n; i++) {

            // generate random index number
            int index = random.nextInt(AlphaNumericString.length());

            // get character specified by index
            // from the string
            char randomChar = AlphaNumericString.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }


        return sb.toString();
    }
}