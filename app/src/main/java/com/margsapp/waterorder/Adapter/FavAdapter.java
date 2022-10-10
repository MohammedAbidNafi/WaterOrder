package com.margsapp.waterorder.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.iosdialog.iOSDialog;
import com.margsapp.iosdialog.iOSDialogListener;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.ProductPageActivity;
import com.margsapp.waterorder.R;

import java.util.ArrayList;
import java.util.EventListener;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Cart> cartArrayList;
    private final FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    EventListener refresh_listener;


    public interface EventListener {
        void refresh_fragment();
    }

    public FavAdapter(Context mContext, ArrayList<Cart> cartArrayList,FirebaseUser firebaseUser) {
        this.mContext = mContext;
        this.cartArrayList = cartArrayList;
        this.firebaseUser = firebaseUser;
    }

    public void addEventListener(EventListener refresh_listener){
        this.refresh_listener = refresh_listener;
    }
    public void removeEventListener(){
        refresh_listener = null;
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.fav_item, parent, false);
        return new FavAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, int position) {

        final Cart cart = cartArrayList.get(position);

        holder.product_title.setText(cart.getTitle());
        holder.product_quantity.setText(cart.getQuantity());
        holder.product_price.setText(cart.getPrice());
        String PID = cart.getPID();
        Glide.with(mContext).load(cart.getImage()).into(holder.product_img);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOSDialog.Builder
                        .with(mContext)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to remove the item from favourites?")
                        .setPositiveText("Yes")
                        .setPostiveTextColor(mContext.getResources().getColor(com.margsapp.iosdialog.R.color.red))
                        .setNegativeText("No")
                        .setNegativeTextColor(mContext.getResources().getColor(com.margsapp.iosdialog.R.color.company_blue))
                        .onPositiveClicked(new iOSDialogListener() {
                            @Override
                            public void onClick(Dialog dialog) {

                                firestore = FirebaseFirestore.getInstance();
                                firestore.collection("Users").document(firebaseUser.getUid()).collection("Fav").document(PID).delete();
                                refresh_listener.refresh_fragment();
                                Toast.makeText(mContext, "Item has been Removed",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .onNegativeClicked(new iOSDialogListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                //Do Nothing
                            }
                        })
                        .isCancellable(true)
                        .build()
                        .show();
            }



        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductPageActivity.class);
                intent.putExtra("PID",PID);
                mContext.startActivity(intent);



                Log.d("PIDNO", PID);

            }
        });




    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_img = itemView.findViewById(R.id.imageView);
        TextView product_title = itemView.findViewById(R.id.title);
        TextView product_quantity = itemView.findViewById(R.id.quantity);
        TextView product_price = itemView.findViewById(R.id.price);
        ImageView delete = itemView.findViewById(R.id.delete);



        public ViewHolder(View view) {
            super(view);



        }
    }


}
