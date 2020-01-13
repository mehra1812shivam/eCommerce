package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public  class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView prodname,proddescription,txtproductprice,txtproductstate;
    public ImageView prodimage;
    public ItemClickListener listener;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        prodimage=(ImageView)itemView.findViewById(R.id.prod_image_s);
        prodname=(TextView)itemView.findViewById(R.id.prod_name_s);
        proddescription=(TextView)itemView.findViewById(R.id.prod_desc_s);
        txtproductprice=(TextView)itemView.findViewById(R.id.prod_pri_s);
        txtproductstate=itemView.findViewById(R.id.prod_state_s);

    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);

    }
}

