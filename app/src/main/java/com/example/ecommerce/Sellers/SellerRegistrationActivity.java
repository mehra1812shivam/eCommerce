package com.example.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {
    private Button seller_already,register;
    private EditText nameinput,phoneinput,emailinput,passwordinput,addressinput;
    private FirebaseAuth mauth;
    private ProgressDialog loadbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        loadbar=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
        seller_already=findViewById(R.id.seller_exist_btn);
        nameinput=findViewById(R.id.seller_name);
        phoneinput=findViewById(R.id.seller_phone);
        emailinput=findViewById(R.id.seller_email);
        passwordinput=findViewById(R.id.seller_password);
        addressinput=findViewById(R.id.seller_address);
        register=findViewById(R.id.seller_register_btn);
        seller_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerRegistration();
            }
        });
    }

    private void SellerRegistration() {
        final String name=nameinput.getText().toString();
        final String phone=phoneinput.getText().toString();
        final String email=emailinput.getText().toString();
        String password=passwordinput.getText().toString();
        passwordinput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        final String address=addressinput.getText().toString();

        if (!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("")){
            loadbar.setTitle("Creating Seller Account");
            loadbar.setMessage("please wait");
            loadbar.setCanceledOnTouchOutside(false);
            loadbar.show();


            mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
                        String sid=mauth.getCurrentUser().getUid();
                        HashMap<String,Object>sellermap=new HashMap<>();
                        sellermap.put("sid",sid);
                        sellermap.put("phone",phone);
                        sellermap.put("email",email);
                        sellermap.put("address",address);
                        sellermap.put("name",name);

                        ref.child("Sellers").child(sid).updateChildren(sellermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadbar.dismiss();

                                Toast.makeText(SellerRegistrationActivity.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }

                }
            });




        }else {
            Toast.makeText(this, "Please fill the complete form", Toast.LENGTH_SHORT).show();
        }

    }
}
