package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check="";
    private TextView resettv;
    private EditText phone,question1,question2;
    private TextView titlequestions;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        check=getIntent().getStringExtra("check");
        resettv=findViewById(R.id.reset_password_tv);
        phone=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        titlequestions=findViewById(R.id.title_of_questions);
        verify=findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        phone.setVisibility(View.GONE);

        if (check.equals("settings")){
            resettv.setText("Set Security Questions");
            titlequestions.setText("Please set answers for the security questions");
            verify.setText("Set");
            DisplayPreviousAnswers();
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAnswers();
                }
            });


        }
        else if (check.equals("login")){
            phone.setVisibility(View.VISIBLE);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyUserforResetting();
                }
            });


        }
    }

    private void VerifyUserforResetting() {
        final String phones=phone.getText().toString();
        final String answe1=question1.getText().toString();
        final String answe2=question2.getText().toString();
        if (!phones.equals("") && !answe1.equals("") && !answe2.equals("")){
            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(phones);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String aphone=dataSnapshot.child("phone").getValue().toString();
                        if (dataSnapshot.hasChild("Security Questions")){
                            String answ1=dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String answ2=dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                            if (!answ1.equals(answe1)){
                                Toast.makeText(ResetPasswordActivity.this, "answer 1 is incorrect", Toast.LENGTH_SHORT).show();
                            }
                            else if (!answ2.equals(answe2)){
                                Toast.makeText(ResetPasswordActivity.this, "answer 2 is incorrect", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newpassword=new EditText(ResetPasswordActivity.this);
                                newpassword.setHint("Write new password here...");
                                builder.setView(newpassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newpassword.getText().toString().equals("")){
                                            ref.child("password").setValue(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ResetPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                        }

                                    }
                                });
                                builder.setNegativeButton("Camcel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });
                                builder.show();
                            }

                        }

                        else {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the security questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number doesn't exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else {
            Toast.makeText(this, "please enter complete details", Toast.LENGTH_SHORT).show();
        }


    }

    private void SetAnswers(){
        String ans1=question1.getText().toString().toLowerCase();
        String ans2=question2.getText().toString().toLowerCase();
        if (question1.equals("") && question2.equals("")){
            Toast.makeText(ResetPasswordActivity.this, "Answer both questions", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineusers.getPhone());
            HashMap<String, Object> userdata = new HashMap<>();
            userdata.put("answer1",ans1);
            userdata.put("answer2",ans2);

            ref.child("Security Questions").updateChildren(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Security Questions set successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }

                }
            });

        }
    }
    private void DisplayPreviousAnswers(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineusers.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String answ1=dataSnapshot.child("answer1").getValue().toString();
                    String answ2=dataSnapshot.child("answer2").getValue().toString();
                    question1.setText(answ1);
                    question2.setText(answ2);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
