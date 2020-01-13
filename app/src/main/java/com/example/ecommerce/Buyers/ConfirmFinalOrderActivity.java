package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText name,phone,address,city;
    private Button confirm;
    private String totalamount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalamount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price ="+totalamount, Toast.LENGTH_SHORT).show();
        confirm=findViewById(R.id.confirm_order);
        name=findViewById(R.id.shipment_name);
        phone=findViewById(R.id.shipment_phone);
        address=findViewById(R.id.shipment_address);
        city=findViewById(R.id.shipment_city);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this, "Enter your full name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this, "Enter your phone number", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this, "Enter your address", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(city.getText().toString())){
            Toast.makeText(this, "Enter your city", Toast.LENGTH_SHORT).show();
        }
        else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String savecurrentdate,savecurrenttime;
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate=currentdate.format(calfordate.getTime());
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calfordate.getTime());

        final DatabaseReference ordersref= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.onlineusers.getPhone());
        HashMap<String,Object>ordersmap=new HashMap<>();
        ordersmap.put("totalAmount",totalamount);
        ordersmap.put("name",name.getText().toString());
        ordersmap.put("phone",phone.getText().toString());
        ordersmap.put("Address",address.getText().toString());
        ordersmap.put("City",city.getText().toString());
        ordersmap.put("date",savecurrentdate);
        ordersmap.put("time",savecurrenttime);
        ordersmap.put("state","not shipped");

        ordersref.updateChildren(ordersmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.onlineusers.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
                }

            }
        });

    }
}
