package com.margsapp.waterorder.Adapter;

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
import com.margsapp.waterorder.Model.Product;
import com.margsapp.waterorder.ProductPageActivity;
import com.margsapp.waterorder.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Product> productList;

    public ProductAdapter(Context mContext, ArrayList<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        return new ProductAdapter.ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        final Product product = productList.get(position);

        holder.product_title.setText(product.getTitle());
        holder.product_quantity.setText(product.getQuantity());
        holder.product_price.setText(product.getPrice());

        Glide.with(mContext).load(product.getImage()).into(holder.product_img);

        String PID = product.getPID();

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
        return productList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView product_img = itemView.findViewById(R.id.product_image);

        TextView product_title = itemView.findViewById(R.id.product_title);
        TextView product_quantity = itemView.findViewById(R.id.product_quantity);
        TextView product_price = itemView.findViewById(R.id.product_price);


        public ViewHolder(View view) {
            super(view);



        }
    }
}
