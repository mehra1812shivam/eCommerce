package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.Sellers.SellerHomeActivity;
import com.example.ecommerce.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button join,login;
    private ProgressDialog loadBar;
    private TextView wanna_seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join=(Button)findViewById(R.id.join_btn);
        Paper.init(this);
        login=(Button)findViewById(R.id.login_btn);
        wanna_seller=findViewById(R.id.wanna_seller);
        wanna_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
        loadBar=new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, RegisterationActivity.class);
                startActivity(i);
            }
        });
        String phonekey=Paper.book().read(Prevalent.phonekey);
        String passwordkey=Paper.book().read(Prevalent.passwordkey);
        if (phonekey!="" && passwordkey!=""){
            if (!TextUtils.isEmpty(phonekey) && !TextUtils.isEmpty(passwordkey)){
                AllowAcess(phonekey,passwordkey);
                loadBar.setTitle("Already Logged In");
                loadBar.setMessage("Please wait");
                loadBar.setCanceledOnTouchOutside(false);
                loadBar.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser !=null){
            Intent intent=new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAcess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()){
                    Users userData=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this,"You are already Logged in",Toast.LENGTH_LONG).show();
                            loadBar.dismiss();
                            Intent id=new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.onlineusers=userData;
                            startActivity(id);

                        }else{
                            Toast.makeText(MainActivity.this,"Password Incorrect",Toast.LENGTH_LONG).show();
                            loadBar.dismiss();
                        }

                    }


                }else {
                    Toast.makeText(MainActivity.this,"Account with these credentials does not exist",Toast.LENGTH_LONG);
                    loadBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
