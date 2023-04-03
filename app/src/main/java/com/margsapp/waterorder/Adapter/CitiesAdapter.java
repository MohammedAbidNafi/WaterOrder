package com.margsapp.waterorder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.waterorder.CitySelector;
import com.margsapp.waterorder.Model.Cities;
import com.margsapp.waterorder.Model.OrderItem;
import com.margsapp.waterorder.R;

import java.util.ArrayList;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder>{

    private final Context mContext;
    private final ArrayList<Cities> citiesArrayList;
    private final FirebaseUser firebaseUser;






    public CitiesAdapter(Context mContext, ArrayList<Cities> citiesArrayList,FirebaseUser firebaseUser) {

        this.mContext = mContext;
        this.citiesArrayList = citiesArrayList;
        this.firebaseUser = firebaseUser;
    }


    @NonNull
    @Override
    public CitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false);
        return new CitiesAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesAdapter.ViewHolder holder, int position) {

        final Cities cities = citiesArrayList.get(position);

        holder.city_name.setText(cities.getName());

        Glide.with(mContext).load(cities.getImage()).into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                CollectionReference dbUsers = firestore.collection("Users");

                dbUsers.document(firebaseUser.getUid()).update("City",cities.getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ((CitySelector)mContext).finish();
                    }
                });
            }
        });






    }

    @Override
    public int getItemCount() {
        return citiesArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView city_name = itemView.findViewById(R.id.cityName);

        ImageView imageView = itemView.findViewById(R.id.image);





        public ViewHolder(View view) {
            super(view);



        }
    }
}
