package com.example.ecommerce.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminHomeActivity;
import com.example.ecommerce.Sellers.AdminCategoryactivity;
import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhone, InputPassword;
    private Button Login;
    private ProgressDialog loadBar;
    private TextView AdminLink, NotAdminLink,Forgetpassword;

    private String parentDbName = "Users";
    private CheckBox chkrememberme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Forgetpassword=findViewById(R.id.forget_password);
        Login = (Button) findViewById(R.id.login_btn2);
        InputPassword = (EditText) findViewById(R.id.login_password);
        InputPhone = (EditText) findViewById(R.id.login_phone);
        AdminLink = (TextView) findViewById(R.id.admin_panel);
        chkrememberme=(CheckBox)findViewById(R.id.rememberchk);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel);
        Forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);

            }
        });

        loadBar = new ProgressDialog(this);
        Paper.init(this);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.setText("LogIn");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";

            }
        });

    }

    private void UserLogin() {
        String phone = InputPhone.getText().toString();
        String password = InputPassword.getText().toString();
        InputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter your password...", Toast.LENGTH_SHORT).show();
        } else {
            loadBar.setTitle("Login Account");
            loadBar.setMessage("Please wait");
            loadBar.setCanceledOnTouchOutside(false);
            loadBar.show();


            AllowAccessToAccount(phone, password);
        }

    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if (chkrememberme.isChecked()){
            Paper.book().write(Prevalent.phonekey,phone);
            Paper.book().write(Prevalent.passwordkey,password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users userData=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                           if (parentDbName.equals("Admins")){
                               Toast.makeText(LoginActivity.this,"welcome admin,You are Logged in",Toast.LENGTH_LONG).show();
                               loadBar.dismiss();
                               Intent hi=new Intent(LoginActivity.this, AdminHomeActivity.class);
                               startActivity(hi);

                           }else if (parentDbName.equals("Users")){
                               Toast.makeText(LoginActivity.this,"You are Logged in",Toast.LENGTH_LONG).show();
                               loadBar.dismiss();
                               Intent id=new Intent(LoginActivity.this, HomeActivity.class);
                               Prevalent.setOnlineusers(userData);
                               startActivity(id);
                           }

                        }else{
                            Toast.makeText(LoginActivity.this,"Password Incorrect",Toast.LENGTH_LONG).show();
                            loadBar.dismiss();
                        }

                    }


                }else {
                    Toast.makeText(LoginActivity.this,"Account with these credentials does not exist",Toast.LENGTH_LONG);
                    loadBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}