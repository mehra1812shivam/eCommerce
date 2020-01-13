package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addtocart;

    private ImageView productImage;
    private TextView prodname,proddescr,prodprice;
    private ElegantNumberButton numberButton;
    private String productID="",state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productID=getIntent().getStringExtra("pid");
        addtocart=findViewById(R.id.add_to_cart_button);
        productImage=findViewById(R.id.prod_image_details);
        prodname=findViewById(R.id.prod_name_details);
        proddescr=findViewById(R.id.prod_description_details);
        prodprice=findViewById(R.id.prod_price_details);
        numberButton=findViewById(R.id.number_btn);

        getProductDetails(productID);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Placed")||state.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "you can puchase more once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else{
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList() {
        String savecurrenttime,savecurrentdate;
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate=currentdate.format(calfordate.getTime());
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calfordate.getTime());

        final DatabaseReference CartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object>cartMap=new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",prodname.getText().toString());
        cartMap.put("price",prodprice.getText().toString());
        cartMap.put("date",savecurrentdate);
        cartMap.put("time",savecurrenttime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");
        CartListRef.child("User View").child(Prevalent.onlineusers.getPhone()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    CartListRef.child("Admin View").child(Prevalent.onlineusers.getPhone()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    });

                }

            }
        });



    }

    private void getProductDetails(String productID) {
        DatabaseReference prodRef= FirebaseDatabase.getInstance().getReference().child("Products");
        prodRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);
                    prodname.setText(products.getPname());
                    prodprice.setText(products.getPrice());
                    proddescr.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState(){
        DatabaseReference ordersref;
        ordersref=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.onlineusers.getPhone());
        ordersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")){
                        state="Order Shipped";



                    }
                    else if (shippingState.equals("not shipped")){
                        state="Order Placed";


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
