package com.margsapp.waterorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.margsapp.waterorder.Adapter.CartAdapter;
import com.margsapp.waterorder.Adapter.FavAdapter;
import com.margsapp.waterorder.Adapter.OrderItemAdapter;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.Model.Order;
import com.margsapp.waterorder.Model.OrderItem;

import java.util.ArrayList;

public class OrderViewActivity extends AppCompatActivity {

    Toolbar toolbar;

    RecyclerView recyclerView;

    FirebaseUser firebaseUser;

    FirebaseFirestore firestore;

    TextView orderID_txt,ordered_on_txt,order_status_txt,order_address_txt,order_Number_txt,order_value_txt;

    ArrayList<OrderItem> orderItemArrayList;

    OrderItemAdapter orderItemAdapter;
    String orderID;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();

        intent = getIntent();

        orderID = intent.getStringExtra("OrderID");

        orderItemArrayList = new ArrayList<>();
        orderItemAdapter = new OrderItemAdapter(getApplicationContext(),orderItemArrayList,firebaseUser);

        recyclerView.setAdapter(orderItemAdapter);

        orderID_txt = findViewById(R.id.order_id);
        order_status_txt = findViewById(R.id.order_status);
        ordered_on_txt = findViewById(R.id.ordered_on);
        order_address_txt = findViewById(R.id.order_address);
        order_Number_txt = findViewById(R.id.order_phone);
        order_value_txt = findViewById(R.id.order_value);

        loadData();


    }


    private void loadData(){



        firestore.collection("Users").document(firebaseUser.getUid()).collection("Order").document(orderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String orderStatus = document.getString("order_status");
                        String orderedOn = document.getString("d_t");
                        String orderAddress = document.getString("address");
                        String orderNumber = document.getString("phoneNumber");
                        String orderValue = String.valueOf(document.get("order_value"));


                        orderID_txt.setText(String.format("Order ID: %s", orderID));
                        order_status_txt.setText(String.format("Current Status: %s", orderStatus));
                        ordered_on_txt.setText(String.format("Ordered on: %s", orderedOn));
                        order_address_txt.setText(orderAddress);
                        order_Number_txt.setText(orderNumber);
                        order_value_txt.setText(String.format("Total Value: %s", orderValue));





                    }
                }
            }
        });



        firestore.collection("Users").document(firebaseUser.getUid()).collection("Order").document(orderID).collection("Items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                orderItemArrayList.clear();
                assert value != null;
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){
                        orderItemArrayList.add(documentChange.getDocument().toObject(OrderItem.class));
                    }



                    orderItemAdapter.notifyDataSetChanged();


                }

            }
        });

    }
}