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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.margsapp.iosdialog.iOSDialog;
import com.margsapp.iosdialog.iOSDialogListener;
import com.margsapp.waterorder.Model.Cart;
import com.margsapp.waterorder.Model.Order;
import com.margsapp.waterorder.OrderViewActivity;
import com.margsapp.waterorder.ProductPageActivity;
import com.margsapp.waterorder.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Order> orderArrayList;
    private final FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    FavAdapter.EventListener refresh_listener;


    public interface EventListener {
        void refresh_fragment();
    }

    public OrderAdapter(Context mContext, ArrayList<Order> orderArrayList,FirebaseUser firebaseUser) {
        this.mContext = mContext;
        this.orderArrayList = orderArrayList;
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
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

        final Order order = orderArrayList.get(position);

        holder.order_id.setText(String.format("%s%s","Order ID: ",order.getOrder_id()));
        holder.order_status.setText(String.format("%s%s","Status: ", order.getOrder_status()));
        holder.order_value.setText(String.format("%s%s%s","Cart Value: ",mContext.getText(R.string.rupee), order.getOrder_value()));
        holder.order_on.setText(String.format("%s%s","Ordered on: ", order.getD_t()));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OrderViewActivity.class);
                intent.putExtra("OrderID",order.getOrder_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });






    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView order_id = itemView.findViewById(R.id.order_id);
        TextView order_status = itemView.findViewById(R.id.order_status);
        TextView order_value = itemView.findViewById(R.id.order_value);
        TextView order_on = itemView.findViewById(R.id.ordered_on);





        public ViewHolder(View view) {
            super(view);



        }
    }
}
