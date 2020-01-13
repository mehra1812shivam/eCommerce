package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {
    private Button search;
    private EditText prodname;
    private RecyclerView searchlist;
    private String Searchinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        search=findViewById(R.id.search_bar_button);
        prodname=findViewById(R.id.search_prod_name);
        searchlist=findViewById(R.id.search_list);
        searchlist.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Searchinput=prodname.getText().toString();
                onStart();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(Searchinput),Products.class).build();

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
                        Intent intent=new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", products.getPid());
                        startActivity(intent);
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
        searchlist.setAdapter(adapter);
        adapter.startListening();




    }
}
