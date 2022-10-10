package com.margsapp.waterorder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.margsapp.waterorder.Adapter.FavAdapter;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.Model.Product;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProductPageActivity extends AppCompatActivity {

    ImageView product_image;

    TextView title, Quantity, description, price;

    ElegantNumberButton quantity;

    AppCompatButton Add_to_Cart,Add_to_Favourites,Remove_from_Favourites,Remove_from_Cart,Go_to_Cart;

    String PID,ImageURL;

    Intent intent;

    LinearLayout bottom,bottom_2;

    FirebaseUser firebaseUser;

    private FirebaseFirestore firestore;
    SparkButton fav_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        createToolBar();

        get_set();

        checkCart_AndFav();

        loadData();





        Add_to_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddToCart();
            }
        });

        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fav_btn.isChecked()){
                    firestore.collection("Users").document(firebaseUser.getUid()).collection("Fav").document(PID)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }else {
                    AddToFav();
                    fav_btn.playAnimation();
                }

            }
        });



        Go_to_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CartActivity.class));
            }
        });

        Remove_from_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
                databaseReference.removeValue();
            }
        });

    }





    private void createToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void get_set() {

        firestore = FirebaseFirestore.getInstance();

        product_image = findViewById(R.id.product_image);

        title = findViewById(R.id.title);

        Quantity = findViewById(R.id.Quantity_);
        description = findViewById(R.id.description);

        price = findViewById(R.id.price);

        quantity = findViewById(R.id.quantity);

        Add_to_Cart = findViewById(R.id.add_to_cart);

        Remove_from_Cart = findViewById(R.id.remove_from_cart);
        Go_to_Cart = findViewById(R.id.Go_to_cart);


        intent = getIntent();
        PID = intent.getStringExtra("PID");

        bottom = findViewById(R.id.bottom);
        bottom_2 = findViewById(R.id.bottom_2);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        fav_btn = findViewById(R.id.spark_button);
    }


    private void loadData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(PID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);






                    title.setText(product.getTitle());
                    description.setText(product.getDesc());
                    Quantity.setText(product.getQuantity());
                    price.setText(product.getPrice());
                    description.setText(product.getDesc());
                    ImageURL = product.getImage();

                    Glide.with(getApplication()).load(product.getImage()).into(product_image);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkCart_AndFav() {


        assert firebaseUser != null;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    bottom_2.setVisibility(View.VISIBLE);
                    bottom.setVisibility(View.GONE);

                }else {
                    bottom.setVisibility(View.VISIBLE);
                    bottom_2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        firestore.collection("Users").document(firebaseUser.getUid()).collection("Fav").document(PID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.exists()) {
                   fav_btn.setChecked(true);
                } else {
                    fav_btn.setChecked(false);
                }
            }
        });


    }




    private void AddToCart() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference();

        HashMap<String, CharSequence> hashMap = new HashMap<String, CharSequence>();
        hashMap.put("PID",PID);
        hashMap.put("Image",ImageURL);
        hashMap.put("Title",title.getText());
        hashMap.put("Quantity",Quantity.getText());
        hashMap.put("Price",price.getText());
        hashMap.put("No",quantity.getNumber());

        assert firebaseUser != null;
        reference.child("Users").child(firebaseUser.getUid()).child("Cart").child(PID).setValue(hashMap);


    }

    private void AddToFav() {



        CollectionReference reference = firestore.collection("Users");

        String No = "";
        // adding our data to our courses object class.
        Cart cart = new Cart(No,PID,price.getText().toString(),Quantity.getText().toString(),title.getText().toString(),ImageURL);

        // below method is use to add data to Firebase Firestore.
        reference.document(firebaseUser.getUid()).collection("Fav").document(PID).set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(ProductPageActivity.this, "Fail to add data \n" + e, Toast.LENGTH_SHORT).show();
            }
        });


    }
}