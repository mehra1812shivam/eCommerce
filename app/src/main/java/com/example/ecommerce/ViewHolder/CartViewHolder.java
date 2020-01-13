package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtproductname,txtproductprice,txtproductquantity;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtproductname=itemView.findViewById(R.id.cart_product_name);
        txtproductprice=itemView.findViewById(R.id.cart_product_price);
        txtproductquantity=itemView.findViewById(R.id.cart_product_quantity);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }
}
