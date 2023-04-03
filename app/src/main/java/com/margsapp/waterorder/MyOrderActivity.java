package com.margsapp.waterorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.margsapp.waterorder.Adapter.FavAdapter;
import com.margsapp.waterorder.Adapter.OrderAdapter;
import com.margsapp.waterorder.Fragments.Favourites;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.Model.Order;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    LinearLayout empty;

    FirebaseUser firebaseUser;

    ArrayList<Order> OrderList;

    FirebaseFirestore firestore;

    OrderAdapter orderAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        empty = findViewById(R.id.empty);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();
        OrderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getApplicationContext(),OrderList,firebaseUser);

        recyclerView.setAdapter(orderAdapter);

        loadData();

    }

    private void loadData() {
        firestore.collection("Users").document(firebaseUser.getUid()).collection("Order").orderBy("d_t", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                OrderList.clear();
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){
                        OrderList.add(documentChange.getDocument().toObject(Order.class));
                    }



                    orderAdapter.notifyDataSetChanged();


                }
                if (OrderList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
            }
        });
    }
}