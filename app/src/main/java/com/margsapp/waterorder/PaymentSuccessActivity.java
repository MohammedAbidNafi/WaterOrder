package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.waterorder.Adapter.CartAdapter;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.Model.Order;
import com.margsapp.waterorder.Model.OrderItem;
import com.margsapp.waterorder.Model.Price;
import com.margsapp.waterorder.Model.Users;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PaymentSuccessActivity extends AppCompatActivity {

    AppCompatButton done;

    Order order;

    FirebaseUser firebaseUser;

    FirebaseFirestore firestore;

    LottieAnimationView successAnim;

    Intent intent;

    String orderID;
    String paymentID;

    String orderNo;
    String dateTime;


    String username;
    String address;
    String number;

    TextView title;
    TextView desc;


    ProgressBar loader;

    int orderValue;

    ArrayList<String> orderList;


    OrderItem orderItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        loader = findViewById(R.id.loader);
        title = findViewById(R.id.order_placed_txt);
        desc = findViewById(R.id.order_desc_txt);

        done = findViewById(R.id.done);

        intent = getIntent();

        orderID = intent.getStringExtra("orderID");
        paymentID = intent.getStringExtra("paymentID");

        successAnim = findViewById(R.id.animationView);




        firestore = FirebaseFirestore.getInstance();

        orderList = new ArrayList<>();




        orderItem = new OrderItem();





        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccessActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        getUserDetails();

    }



    private void getUserDetails() {

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String Username = document.getString("username");
                        String Address = document.getString("address_STR");
                        String PhoneNo = document.getString("phoneNumber");

                        username = Username;
                        address = Address;
                        number = PhoneNo;


                    }

                    getDateAndTime();
                }
            }
        });
    }

    private void getDateAndTime() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");

        // on below line we are creating a variable
        // for current date and time and calling a simple date format in it.
        dateTime = sdf.format(new Date());

        getOrderValue();

    }

    private void getOrderValue() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("CartValue");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Price price = snapshot.getValue(Price.class);

                    assert price != null;
                    orderValue = price.getTotalPrice();
                    Log.d("Value", String.valueOf(price.getTotalPrice()));


                    updateOrder();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getOrderItems() {

        CollectionReference dataReference = firestore.collection("Users");

        Query query = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cart cart = dataSnapshot.getValue(Cart.class);

                    assert cart != null;

                    orderItem = new OrderItem(cart.getPrice(),cart.getPID(),cart.getTitle(),cart.getQuantity(),cart.getNo(),cart.getImage());
                    dataReference.document(firebaseUser.getUid()).collection("Order").document(orderNo).collection("Items").document(cart.getPID()).set(orderItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            RemoveCart();
                        }
                    });


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }



    private void updateOrder() {

        CollectionReference dataReference = firestore.collection("Users");


        // adding our data to our courses object class.
        Order order = new Order(orderNo, orderValue, "Ordered", orderList,dateTime,username,number,address);

        // below method is use to add data to Firebase Firestore.
        dataReference.document(firebaseUser.getUid()).collection("Order").document(orderNo).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                getOrderItems();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(PaymentSuccessActivity.this, "Fail to add data \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RemoveCart() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("Users").child(firebaseUser.getUid()).child("CartValue").child("TotalPrice").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart");
                databaseReference2.removeValue();
                desc.setText("Your order #" +orderNo+ " has been placed successfully");
                title.setText("Order Placed");
                successAnim.playAnimation();

                done.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);

            }
        });

    }

    private String Randomizer(int n) {
        String AlphaNumericString = "01234567890";

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

    @Override
    protected void onStart() {
        super.onStart();
        orderNo = Randomizer(5);
    }

}