package com.example.ecommerce.Admin;

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

import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView orderslist;
    private DatabaseReference ordersref;
    private DatabaseReference cartlistref;

    private String userID="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersref= FirebaseDatabase.getInstance().getReference().child("Orders");
        cartlistref=FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
        orderslist=findViewById(R.id.orders_list);
        orderslist.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders>options=new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersref,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder>adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminOrdersViewHolder adminOrdersViewHolder, final int i, @NonNull final AdminOrders adminOrders) {
                adminOrdersViewHolder.User_Name.setText("Name :"+adminOrders.getName());
                adminOrdersViewHolder.Userphonenumber.setText("Phone :"+adminOrders.getPhone());
                adminOrdersViewHolder.Usershippingaddress.setText("Shipping Address :"+adminOrders.getAddress()+" "+adminOrders.getCity());
                adminOrdersViewHolder.Userdatetime.setText("Order at"+adminOrders.getDate()+" "+adminOrders.getTime());
                adminOrdersViewHolder.Total_Price.setText("Total Amount :"+adminOrders.getTotalAmount());
                adminOrdersViewHolder.ShowOrdersbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID=getRef(i).getKey();

                        Intent intent=new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });
                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped the product");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which==0){
                                    String uID=getRef(i).getKey();
                                    RemoveOrder(uID);
                                    cartlistref.child(uID).removeValue();


                                }
                                else {
                                    finish();
                                }

                            }
                        });
                        builder.show();
                    }
                });



            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        orderslist.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uID) {
        ordersref.child(uID).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView User_Name,Total_Price,Userphonenumber,Userdatetime,Usershippingaddress;
        public Button ShowOrdersbtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            User_Name=itemView.findViewById(R.id.order_User_name);
            Total_Price=itemView.findViewById(R.id.order_total_price);
            Userphonenumber=itemView.findViewById(R.id.order_phone_number);
            Userdatetime=itemView.findViewById(R.id.order_date_time);
            Usershippingaddress=itemView.findViewById(R.id.order_address_city);
            ShowOrdersbtn=itemView.findViewById(R.id.showallproductsinfo);
        }
    }
}
