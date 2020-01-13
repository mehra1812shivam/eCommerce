package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductsActivity extends AppCompatActivity {
    private RecyclerView productslist;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartlistref;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);
        userID=getIntent().getStringExtra("uid");

        productslist=findViewById(R.id.products_list);
        productslist.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        productslist.setLayoutManager(layoutManager);

        cartlistref= FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart>options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartlistref,Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtproductquantity.setText("Quantity :" +cart.getQuantity());
                cartViewHolder.txtproductname.setText("Name" +cart.getPname());
                cartViewHolder.txtproductprice.setText("Price" +cart.getPrice());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(v);
                return holder;
            }
        };
        productslist.setAdapter(adapter);
        adapter.startListening();
    }
}
