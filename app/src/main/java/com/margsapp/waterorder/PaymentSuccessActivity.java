package com.margsapp.waterorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.margsapp.waterorder.Model.Order;

public class PaymentSuccessActivity extends AppCompatActivity {

    AppCompatButton done;

    Order order;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        done = findViewById(R.id.done);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("Users").child(firebaseUser.getUid()).child("CartValue").child("TotalPrice").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart");
                databaseReference2.removeValue();

            }
        });


    }
}