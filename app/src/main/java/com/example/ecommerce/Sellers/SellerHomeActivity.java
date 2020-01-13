package com.example.ecommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Admin.AdminNewOrdersActivity;
import com.example.ecommerce.Admin.CheckNewProductActivity;
import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ItemViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SellerHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unapprovedproductsref;

    private TextView mTextMessage;
    private Button checkorder;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intents1=new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                    startActivity(intents1);
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_add:
                    Intent intents=new Intent(SellerHomeActivity.this, AdminCategoryactivity.class);
                    startActivity(intents);
                    return true;
                case R.id.navigation_logout:
                    final FirebaseAuth mauth;
                    mauth=FirebaseAuth.getInstance();
                    mauth.signOut();
                    Intent intent=new Intent(SellerHomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        checkorder=findViewById(R.id.check_s);

        checkorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView=findViewById(R.id.recycle_seller_home);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        unapprovedproductsref= FirebaseDatabase.getInstance().getReference().child("Products");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unapprovedproductsref.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();
        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.prodname.setText(products.getPname());
                productViewHolder.proddescription.setText(products.getDescription());
                productViewHolder.txtproductstate.setText("State:"+products.getProductstate());
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
                        AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Do you want to Delete this product");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    DeleteProduct(productID);

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
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);
                ItemViewHolder holder=new ItemViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void DeleteProduct(String productID) {
        unapprovedproductsref.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SellerHomeActivity.this, "Item is deleted successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
