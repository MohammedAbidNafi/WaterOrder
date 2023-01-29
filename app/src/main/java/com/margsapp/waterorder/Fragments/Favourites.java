package com.margsapp.waterorder.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.margsapp.waterorder.Adapter.FavAdapter;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.R;

import java.util.ArrayList;

public class Favourites extends Fragment implements FavAdapter.EventListener{

    RecyclerView recyclerView;
    LinearLayout empty;

    FirebaseUser firebaseUser;

    ArrayList<Cart> FavList;

    FirebaseFirestore firestore;

    FavAdapter favAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        empty = view.findViewById(R.id.empty);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();
        FavList = new ArrayList<>();
        favAdapter = new FavAdapter(getActivity(),FavList,firebaseUser);

        recyclerView.setAdapter(favAdapter);
        favAdapter.addEventListener(Favourites.this);
        loadData();





        return view;
    }

    private void loadData() {



        firestore.collection("Users").document(firebaseUser.getUid()).collection("Fav").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                FavList.clear();
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){
                        FavList.add(documentChange.getDocument().toObject(Cart.class));
                    }



                    favAdapter.notifyDataSetChanged();


                }
                if (FavList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
            }
        });






    }


    @Override
    public void refresh_fragment() {

        loadData();


    }



    public void onDestroy() {
        super.onDestroy();
        favAdapter.removeEventListener();
    }
}