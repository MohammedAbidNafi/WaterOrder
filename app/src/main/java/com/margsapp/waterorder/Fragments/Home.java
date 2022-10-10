package com.margsapp.waterorder.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.margsapp.waterorder.R;


import org.w3c.dom.Text;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;


public class Home extends Fragment {


    //ImageCarousel imageCarousel;

    //CircleIndicator2 indicator;


    TextView Product_Title_1,Product_Title_2,Product_Title_3,Product_Title_4,Product_Title_5,Product_Title_6,Product_Title_7,Product_Title_8,Product_Title_9;

    TextView Product_SubTitle1,Product_SubTitle2,Product_SubTitle3,Product_SubTitle4,Product_SubTitle5,Product_SubTitle6,Product_SubTitle7,Product_SubTitle8,Product_SubTitle9;

    TextView Price_1,Price_2,Price_3,Price_4,Price_5,Price_6,Price_7,Price_8,Price_9;


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


        Product_Title_1 = view.findViewById(R.id.product_title_1);

        Product_SubTitle1 = view.findViewById(R.id.product_subtitle_1);

        Price_1 = view.findViewById(R.id.price_1);

        loadData();




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

                    productList.add(product);

                }

                productAdapter = new ProductAdapter(getContext(), productList);
                recyclerView.setAdapter(productAdapter);
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }
}