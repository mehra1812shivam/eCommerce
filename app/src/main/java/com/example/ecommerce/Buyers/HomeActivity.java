package com.example.ecommerce.Buyers;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecommerce.Admin.AdminMaintainProductsActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference prodsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            type=getIntent().getExtras().get("Admin").toString();

        }


        prodsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin")){
                    Intent intent=new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview=navigationView.getHeaderView(0);
        TextView usernametv=headerview.findViewById(R.id.profile_name);
        CircleImageView profileImageView=headerview.findViewById(R.id.profile_image);
        if (!type.equals("Admin")){
            usernametv.setText(Prevalent.onlineusers.getName());

            Picasso.get().load(Prevalent.onlineusers.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        }

        recyclerView=findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected  void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(prodsRef.orderByChild("productstate").equalTo("Approved"),Products.class).build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.prodname.setText(products.getPname());
                productViewHolder.proddescription.setText(products.getDescription());
                productViewHolder.txtproductprice.setText("Price =" + products.getPrice() );
                Picasso.get().load(products.getImage()).into(productViewHolder.prodimage);



                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals("Admin")){
                            Intent intent=new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);


                        }
                        else{
                            Intent intent=new Intent(HomeActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);


                        }


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }

            // Handle the camera action
        } else if (id == R.id.nav_search) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this, SearchProductsActivity.class);
                startActivity(intent);

            }

        } else if (id == R.id.categories) {

        } else if (id == R.id.nav_settings) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);

            }

        } else if (id == R.id.nav_logout) {
            if (!type.equals("Admin")){
                Paper.book().destroy();
                Intent intent=new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
