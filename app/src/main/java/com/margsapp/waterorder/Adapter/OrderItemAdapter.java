package com.margsapp.waterorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.waterorder.Model.Order;
import com.margsapp.waterorder.Model.OrderItem;
import com.margsapp.waterorder.OrderViewActivity;
import com.margsapp.waterorder.R;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{

    private final Context mContext;
    private final ArrayList<OrderItem> orderItemArrayList;
    private final FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    FavAdapter.EventListener refresh_listener;


    public interface EventListener {
        void refresh_fragment();
    }

    public OrderItemAdapter(Context mContext, ArrayList<OrderItem> orderItemArrayList,FirebaseUser firebaseUser) {

        this.mContext = mContext;
        this.orderItemArrayList = orderItemArrayList;
        this.firebaseUser = firebaseUser;
    }

    public void addEventListener(FavAdapter.EventListener refresh_listener){
        this.refresh_listener = refresh_listener;
    }
    public void removeEventListener(){
        refresh_listener = null;
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.order_item_item, parent, false);
        return new OrderItemAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {

        final OrderItem orderItem = orderItemArrayList.get(position);

        holder.item_title.setText(orderItem.getTitle());
        holder.item_quan.setText(orderItem.getQuantity());
        holder.item_price.setText(String.format("%s%s",mContext.getText(R.string.rupee), orderItem.getPrice()));
        holder.item_no.setText(String.format("%s%s","Qty: ", orderItem.getNo()));

        Glide.with(mContext).load(orderItem.getImage()).into(holder.imageView);






    }

    @Override
    public int getItemCount() {
        return orderItemArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView item_title = itemView.findViewById(R.id.title);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        TextView item_quan = itemView.findViewById(R.id.quantity);
        TextView item_price = itemView.findViewById(R.id.price);
        TextView item_no = itemView.findViewById(R.id.no);





        public ViewHolder(View view) {
            super(view);



        }
    }
}
