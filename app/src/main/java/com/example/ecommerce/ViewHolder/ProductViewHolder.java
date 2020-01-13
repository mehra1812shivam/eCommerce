package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public  class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView prodname,proddescription,txtproductprice;
    public ImageView prodimage;
    public ItemClickListener listener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        prodimage=(ImageView)itemView.findViewById(R.id.prod_image);
        prodname=(TextView)itemView.findViewById(R.id.prod_name);
        proddescription=(TextView)itemView.findViewById(R.id.prod_desc);
        txtproductprice=(TextView)itemView.findViewById(R.id.prod_pri);

    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}
