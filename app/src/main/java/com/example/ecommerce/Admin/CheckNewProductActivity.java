package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CheckNewProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unapprovedproductsref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_product);
        recyclerView=findViewById(R.id.admin_product_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        unapprovedproductsref= FirebaseDatabase.getInstance().getReference().child("Products");



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unapprovedproductsref.orderByChild("productstate").equalTo("Not Approved"),Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.prodname.setText(products.getPname());
                productViewHolder.proddescription.setText(products.getDescription());
                productViewHolder.txtproductprice.setText("Price =" + products.getPrice() + "$");
                Picasso.get().load(products.getImage()).into(productViewHolder.prodimage);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productID=products.getPid();
                        CharSequence options[]=new CharSequence[]{
                                "Yes",
                                "No"


                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CheckNewProductActivity.this);
                        builder.setTitle("Do you want to approve this product");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    ChangeProductState(productID);

                                }
                                if (which==1){

                                }

                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout,parent,false);
                ProductViewHolder holder=new ProductViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void ChangeProductState(String productID) {
        unapprovedproductsref.child(productID).child("productstate").setValue("Approved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CheckNewProductActivity.this, "Item is approved and available for sale", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
