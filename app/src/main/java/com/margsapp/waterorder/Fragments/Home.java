package com.margsapp.waterorder.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.compose.runtime.snapshots.Snapshot;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.margsapp.waterorder.Adapter.ProductAdapter;
import com.margsapp.waterorder.Model.Product;
import com.margsapp.waterorder.ProductPageActivity;
import com.margsapp.waterorder.R;


import org.w3c.dom.Text;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;


public class Home extends Fragment {


    //ImageCarousel imageCarousel;

    //CircleIndicator2 indicator;


    TextView BS_Title;

    TextView BS_SubTitle;

    TextView BS_Price;

    ImageView BS_Image;
    int Product_Price;

    String BSID;
    String BS_img_url;

    CardView BS_Card;




    private int[] images = {R.drawable.img_1,
            R.drawable.img_2, R.drawable.img_3};

    RecyclerView recyclerView;
    ArrayList<Product> productList;
    ProductAdapter productAdapter;


    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting the latest products");
        progressDialog.show();


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        productList = new ArrayList<>();


        CarouselView carouselView = view.findViewById(R.id.carouselView);

        carouselView.setSize(images.length);
        carouselView.setResource(R.layout.center_carousel_item);
        carouselView.setAutoPlay(true);
        carouselView.setAutoPlayDelay(8000);
        carouselView.enableSnapping(true);
        carouselView.setIndicatorSelectedColor(getActivity().getResources().getColor(R.color.primary));
        carouselView.setIndicatorUnselectedColor(getActivity().getResources().getColor(R.color.secondary));
        carouselView.setScaleOnScroll(true);
        carouselView.setIndicatorAnimationType(IndicatorAnimationType.WORM);
        carouselView.setCarouselOffset(OffsetType.CENTER);
        carouselView.setCarouselViewListener(new CarouselViewListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onBindView(View view, int position) {
                // Example here is setting up a full image carousel
                ImageView imageView = view.findViewById(R.id.imageView);
                imageView.setImageDrawable(getResources().getDrawable(images[position]));
            }
        });
        // After you finish setting up, show the CarouselView
        carouselView.show();


        BS_Title = view.findViewById(R.id.bs_title);

        BS_SubTitle = view.findViewById(R.id.bs_subtitle);

        BS_Price = view.findViewById(R.id.bs_price);

        BS_Image = view.findViewById(R.id.bs_img);

        BS_Card = view.findViewById(R.id.BestSellingCard);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("Products").document("BS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        BSID = document.getString("PID");

                        if(BSID != null){
                            loadData();
                        }


                    }
                }
            }
        });


        BS_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductPageActivity.class);
                intent.putExtra("PID",BSID);
                startActivity(intent);
            }
        });









        return view;
    }

    private void loadData() {


        productList.clear();


        Query query = FirebaseDatabase.getInstance().getReference("Products").orderByChild("Order");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Product product = snapshot1.getValue(Product.class);

                    assert product != null;
                    if(!Objects.equals(product.getTitle(), "BS")){
                        productList.add(product);
                    }


                }

                productAdapter = new ProductAdapter(getContext(), productList);
                recyclerView.setAdapter(productAdapter);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(BSID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    Product product = snapshot.getValue(Product.class);

                    assert product != null;
                    BS_Title.setText(product.getTitle());
                    BS_SubTitle.setText(product.getQuantity());
                    BS_Price.setText(String.format("%s%s", getActivity().getText(R.string.rupee), String.valueOf(product.getPrice())));
                    Glide.with(requireContext()).load(product.getImage()).into(BS_Image);

                    BS_img_url = product.getImage();
                    Product_Price = product.getPrice();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });





    }
}