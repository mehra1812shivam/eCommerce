package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterationActivity extends AppCompatActivity {
    private Button create;
    private EditText name,phone,password;
    private ProgressDialog loadbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        create=(Button)findViewById(R.id.register_btn);
        name=(EditText)findViewById(R.id.register_name);
        phone=(EditText)findViewById(R.id.register_phone);
        password=(EditText)findViewById(R.id.register_password);
        loadbar=new ProgressDialog(this);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAccount();
            }
        });

    }
    private void NewAccount(){
        String names =name.getText().toString();
        String phones = phone.getText().toString();
        String passwords = password.getText().toString();
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if (TextUtils.isEmpty(names)){
            Toast.makeText(RegisterationActivity.this,"Enter your name",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(phones)){
            Toast.makeText(RegisterationActivity.this,"Enter your phone number",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(passwords)){
            Toast.makeText(RegisterationActivity.this,"Enter your password",Toast.LENGTH_LONG).show();
        }else {
            loadbar.setTitle("Create Account");
            loadbar.setMessage("please wait");
            loadbar.setCanceledOnTouchOutside(false);
            loadbar.show();
            checkPhoneNumber(names,phones,passwords);

        }

    }

    private void checkPhoneNumber(final String names, final String phones, final String passwords) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phones).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phones);
                    userdataMap.put("password", passwords);
                    userdataMap.put("name", names);
                    RootRef.child("Users").child(phones).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterationActivity.this,"Congratulation, Account Created",Toast.LENGTH_LONG).show();
                                loadbar.dismiss();
                                Intent ib=new Intent(RegisterationActivity.this, LoginActivity.class);
                                startActivity(ib);
                            }else {
                                Toast.makeText(RegisterationActivity.this,"Network Issue,Please Try again",Toast.LENGTH_LONG).show();
                                loadbar.dismiss();
                            }

                        }
                    });


                }else {
                    Toast.makeText(RegisterationActivity.this,"This Number Already Exists",Toast.LENGTH_LONG).show();
                    loadbar.dismiss();
                    Intent ia=new Intent(RegisterationActivity.this, MainActivity.class);
                    startActivity(ia);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
