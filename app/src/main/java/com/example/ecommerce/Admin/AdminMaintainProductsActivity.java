package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.AdminCategoryactivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button applychanges,deleteproduct;
    private EditText name,price,descr;
    private ImageView imageView;
    private String productID="";
    private DatabaseReference prodref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);
        applychanges=findViewById(R.id.prod_maintain_btn);
        name=findViewById(R.id.prod_name_maintain);

        price=findViewById(R.id.prod_pri_maintain);

        descr=findViewById(R.id.prod_desc_maintain);
        imageView=findViewById(R.id.prod_image_maintain);

        productID=getIntent().getStringExtra("pid");
        deleteproduct=findViewById(R.id.delete_prod_btn);

        prodref= FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        displayspecificinfo();

        applychanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });

        deleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteProduct();
            }
        });



    }

    private void DeleteProduct() {
        prodref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent=new Intent(AdminMaintainProductsActivity.this, AdminCategoryactivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductsActivity.this, "Product is deleted successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void ApplyChanges() {
        String pName=name.getText().toString();
        String pPrice=price.getText().toString();
        String pDescription=descr.getText().toString();
        if (pName.equals("")){
            Toast.makeText(this, "Write product name", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals("")){
            Toast.makeText(this, "Write the price of product", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.equals("")){
            Toast.makeText(this, "Give product's description", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> productmap=new HashMap<>();
            productmap.put("pid",productID);

            productmap.put("description",pDescription);

            productmap.put("price",pPrice);
            productmap.put("pname",pName);
            prodref.updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminMaintainProductsActivity.this,AdminCategoryactivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });

        }

    }

    private void displayspecificinfo() {
        prodref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String pName=dataSnapshot.child("pname").getValue().toString();
                    String pPrice=dataSnapshot.child("price").getValue().toString();
                    String pDescription=dataSnapshot.child("description").getValue().toString();
                    String pImage=dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    descr.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
