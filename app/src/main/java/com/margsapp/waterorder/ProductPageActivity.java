package com.margsapp.waterorder;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

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
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.Model.Price;
import com.margsapp.waterorder.Model.Product;
import com.varunest.sparkbutton.SparkButton;

import java.util.HashMap;
import java.util.Objects;

public class ProductPageActivity extends AppCompatActivity {

    ImageView product_image;

    TextView title, Quantity, description, price;

    ElegantNumberButton quantity;

    AppCompatButton Add_to_Cart,Add_to_Favourites,Remove_from_Favourites,Remove_from_Cart,Go_to_Cart;

    String PID,ImageURL,No;

    int Product_Price;

    int TotalPrice;

    Intent intent;

    LinearLayout bottom,bottom_2;

    FirebaseUser firebaseUser;

    Product product_;

    boolean add_to_cart = false;

    private FirebaseFirestore firestore;
    SparkButton fav_btn;

    @Override
    protected void onStart() {
        super.onStart();

        loadData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        createToolBar();

        get_set();

        checkCart_AndFav();



        product_ = new Product();



        quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                if(add_to_cart){
                    if(newValue > oldValue){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("No",String.valueOf(newValue));
                        databaseReference.updateChildren(hashMap);


                        int FinalPrice = TotalPrice + product_.getPrice();
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        databaseReference1.child("Users").child(firebaseUser.getUid()).child("CartValue").child("TotalPrice").setValue(FinalPrice);



                    }

                    if(oldValue > newValue){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("No",String.valueOf(newValue));
                        databaseReference.updateChildren(hashMap);

                        int FinalPrice = TotalPrice - product_.getPrice();
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        databaseReference1.child("Users").child(firebaseUser.getUid()).child("CartValue").child("TotalPrice").setValue(FinalPrice);


                    }
                }

            }
        });





        Add_to_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getTotalPrice();


                int quantity= Integer.parseInt(ProductPageActivity.this.quantity.getNumber());

                int price = Product_Price * quantity;
                int AddedPrice = TotalPrice + price;
                Log.d("Quantity", Integer.toString(TotalPrice));

                AddToCart(AddedPrice);
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

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Cart cart = snapshot.getValue(Cart.class);
                        assert cart != null;
                        No = cart.getNo();
                        Product_Price = cart.getPrice();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                RemoveFromCart(TotalPrice);





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

    private void loadData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(PID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);



                    title.setText(product.getTitle());
                    description.setText(product.getDesc());
                    Quantity.setText(product.getQuantity());
                    price.setText(String.format("%s%s", getApplicationContext().getText(R.string.rupee), String.valueOf(product.getPrice())));
                    description.setText(product.getDesc());
                    ImageURL = product.getImage();
                    Product_Price = product.getPrice();

                    Glide.with(getApplication()).load(product.getImage()).into(product_image);




                product_.setPID(product.getPID());
                product_.setTitle(product.getTitle());
                product_.setDesc(product.getDesc());
                product_.setQuantity(product.getPID());
                product_.setPrice(product.getPrice());
                product_.setImage(product.getImage());


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
                        TotalPrice = price.getTotalPrice();
                    }else {
                        TotalPrice = 0;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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




    private void checkCart_AndFav() {


        assert firebaseUser != null;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    bottom_2.setVisibility(View.VISIBLE);
                    bottom.setVisibility(View.GONE);

                    Cart cart = snapshot.getValue(Cart.class);
                    No = cart.getNo();
                    add_to_cart = true;

                }else {
                    bottom.setVisibility(View.VISIBLE);
                    bottom_2.setVisibility(View.GONE);
                    add_to_cart = false;
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






    private void AddToCart(int AddedPrice) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference();



        HashMap hashMap = new HashMap();
        hashMap.put("PID",PID);
        hashMap.put("Image",ImageURL);
        hashMap.put("Title",title.getText());
        hashMap.put("Quantity",Quantity.getText());
        hashMap.put("Price",Product_Price);
        hashMap.put("No",quantity.getNumber());



        assert firebaseUser != null;
        reference.child("Users").child(firebaseUser.getUid()).child("Cart").child(PID).setValue(hashMap);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(firebaseUser.getUid()).child("CartValue").child("TotalPrice").setValue(AddedPrice);


    }


    private void RemoveFromCart(int totalPrice) {




        int Price_to_be_removed = Integer.parseInt(No) * Product_Price;

        int FinalPrice = TotalPrice - Price_to_be_removed;
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        databaseReference1.child("Users").child(firebaseUser.getUid()).child("CartValue").child("TotalPrice").setValue(FinalPrice).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Cart").child(PID);
                databaseReference2.removeValue();

                loadData();
            }
        });




    }

    private void AddToFav() {



        CollectionReference reference = firestore.collection("Users");

        String No = "";
        // adding our data to our courses object class.
        Cart cart = new Cart(No,PID,Product_Price,Quantity.getText().toString(),title.getText().toString(),ImageURL);

        // below method is use to add data to Firebase Firestore.
        reference.document(firebaseUser.getUid()).collection("Fav").document(PID).set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                fav_btn.playAnimation();

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