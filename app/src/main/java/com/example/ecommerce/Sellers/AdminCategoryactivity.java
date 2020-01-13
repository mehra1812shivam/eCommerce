package com.example.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ecommerce.R;

public class AdminCategoryactivity extends AppCompatActivity {
    private ImageView tshirts,sports,femaledresses,sweaters;
    private ImageView glasses,hats,wallets,shoes;
    private ImageView headphones,laptops,watches,mobiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categoryactivity);

        tshirts=findViewById(R.id.tshirts);
        sports=findViewById(R.id.sportstshirts);
        femaledresses=findViewById(R.id.femaledresses);
        sweaters=findViewById(R.id.sweaters);
        glasses=findViewById(R.id.glasses);
        hats=findViewById(R.id.hats);
        wallets=findViewById(R.id.purses);
        shoes=findViewById(R.id.shoes);
        headphones=findViewById(R.id.headphones);
        laptops=findViewById(R.id.laptops);
        watches=findViewById(R.id.watches);
        mobiles=findViewById(R.id.mobiles);


        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","tshirts");
                startActivity(intent);
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","sports Tshirts");
                startActivity(intent);

            }
        });
        femaledresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","female dresses");
                startActivity(intent);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","sweaters");
                startActivity(intent);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });
        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });
        wallets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","Wallets and Purses");
                startActivity(intent);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","Headphones");
                startActivity(intent);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });
        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryactivity.this, SellerAddNewProduct.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
    }
}
