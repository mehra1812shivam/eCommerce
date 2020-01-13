package com.example.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private Button seller_login;
    private EditText emailInput,passwordInput;
    private ProgressDialog loadbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        mAuth=FirebaseAuth.getInstance();

        seller_login=findViewById(R.id.seller_login_btn);
        passwordInput=findViewById(R.id.seller_password_login);
        emailInput=findViewById(R.id.seller_email_login);
        loadbar=new ProgressDialog(this);

        seller_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsSeller();
            }
        });

    }

    private void LoginAsSeller() {
        final String email=emailInput.getText().toString();
        final String password=passwordInput.getText().toString();
        passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
        if (!email.equals("") && !password.equals("") ){
            loadbar.setTitle("Seller Account Log IN");
            loadbar.setMessage("please wait");
            loadbar.setCanceledOnTouchOutside(false);
            loadbar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent=new Intent(SellerLoginActivity.this, SellerHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }else{
            Toast.makeText(this, "Fill the credentials to Log IN", Toast.LENGTH_SHORT).show();
        }


    }
}
