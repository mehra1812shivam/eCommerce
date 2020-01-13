package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextprocess;
    private TextView totalamount,msg1;
    private int totalprice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextprocess=findViewById(R.id.next_process);
        totalamount=findViewById(R.id.total_price);
        msg1=findViewById(R.id.msg1);
        nextprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(totalprice));
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart>options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View").child(Prevalent.onlineusers.getPhone()).child("Products"),Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                cartViewHolder.txtproductquantity.setText("Quantity :" +cart.getQuantity());
                cartViewHolder.txtproductname.setText("Name" +cart.getPname());
                cartViewHolder.txtproductprice.setText("Price" +cart.getPrice());

                int price_of_one_product=((Integer.parseInt(cart.getPrice().replaceAll("\\D+",""))))* Integer.parseInt(cart.getQuantity());
                totalprice=totalprice+price_of_one_product;

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "EDIT",
                                "REMOVE"

                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    Intent intent=new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }
                                if (which==1){
                                    cartListRef.child("User View").child(Prevalent.onlineusers.getPhone()).child("Products").child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(CartActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }

                            }
                        });
                        builder.show();
                    }
                });totalamount.setText("Total Price="+String.valueOf(totalprice));

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(v);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState(){
        DatabaseReference ordersref;
        ordersref=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.onlineusers.getPhone());
        ordersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    String UserName=dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){
                        totalamount.setText("Dear"+ UserName +"\n order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        msg1.setText("Congatulations,your order is shipped successfully,soon you will replace it at your place");
                        nextprocess.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "you can puchase more,once you recieve your final order", Toast.LENGTH_SHORT).show();

                    }
                    else if (shippingState.equals("not shipped")){
                        totalamount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        msg1.setVisibility(View.VISIBLE);
                        nextprocess.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "you can puchase more,once you recieve your final order", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
