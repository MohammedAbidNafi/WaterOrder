package com.margsapp.waterorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.margsapp.waterorder.Fragments.Cart;
import com.margsapp.waterorder.Fragments.Home;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

public class CartActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    Toolbar toolbar;

    Checkout checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        loadFragment(new Cart());
    }


    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {



        try{
            Intent intent = new Intent(CartActivity.this,PaymentSuccessActivity.class);
            intent.putExtra("orderID",paymentData.getOrderId());
            intent.putExtra("paymentID",paymentData.getPaymentId());
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Payment success in Cart " + s, Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Something went wrong please try again later" + e,Toast.LENGTH_SHORT).show();

            Log.e("Razorpay Error", e.getMessage());
        }




    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}