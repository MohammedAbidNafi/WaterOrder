package com.margsapp.waterorder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.waterorder.Adapter.CartAdapter;
import com.margsapp.waterorder.Adapter.ProductAdapter;
import com.margsapp.waterorder.Model.Price;
import com.margsapp.waterorder.Model.Product;
import com.margsapp.waterorder.PaymentSuccessActivity;
import com.margsapp.waterorder.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Cart extends Fragment  {


    ArrayList<com.margsapp.waterorder.Model.Cart> Cartlist;
    CartAdapter cartAdapter;

    RecyclerView recyclerView;
    CardView coupon,price_summary;
    FirebaseUser firebaseUser;

    TextView total_price;

    Checkout checkout;

    AppCompatButton checkout_btn;

    LinearLayout empty;



    String Username,PhoneNumber;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        total_price = view.findViewById(R.id.total_price);
        //coupon = view.findViewById(R.id.coupon);
        empty = view.findViewById(R.id.empty);
        price_summary = view.findViewById(R.id.price_summary);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Cartlist = new ArrayList<>();


        checkout_btn = view.findViewById(R.id.checkout);

        loadData();


        loadPayement();


        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayement();
            }
        });


        return view;
    }

    private void loadData() {



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
                if(Cartlist.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    //coupon.setVisibility(View.GONE);
                    price_summary.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);

                }
                if(!Cartlist.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    //coupon.setVisibility(View.VISIBLE);
                    price_summary.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }




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

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        PhoneNumber = document.getString("phoneNumber");
                        Username = document.getString("Username");




                    }
                }
            }
        });



    }

    private void loadPayement(){



        Checkout.preload(requireContext());

        checkout = new Checkout();

        checkout.setKeyID("rzp_test_H2r4ZREd5wGlhu");


    }


    public void startPayement(){
        Checkout checkout = new Checkout();


        checkout.setImage(R.mipmap.ic_launcher_round);
        final Activity activity = getActivity();


        try {


            JSONObject options = new JSONObject();

            options.put("name", "Water Order");
            options.put("description","Order water");
            options.put("theme.color", "#276583");
            options.put("currency","INR");
            //options.put("prefill.contact",PhoneNumber);
            options.put("send_sms_hash",true);
            options.put("prefill.name", Username);
            options.put("amount",total_price.getText().toString()+"00");

            checkout.open(activity,options);
        }catch (Exception e){
            Log.e("Error with Razorpay","Error starting Razorpay", e);
        }
    }





}