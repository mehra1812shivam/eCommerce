package com.example.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullname,User_phone,User_address;
    private TextView close,update,changeprofile;
    private Uri imageUri;
    private StorageTask uploadTask;
    private Button securityquestion;
    private String myUrl="";
    private StorageReference storageProfilePictureRef;
    private String checker="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        securityquestion=findViewById(R.id.security_questions_btn);
        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile picture");
        profileImageView=findViewById(R.id.settings_image);
        fullname=findViewById(R.id.settings_full_name);
        User_phone=findViewById(R.id.settings_phone_number);
        User_address=findViewById(R.id.address);
        close=findViewById(R.id.close);
        update=findViewById(R.id.update);
        changeprofile=findViewById(R.id.image_change);
        securityquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
            }
        });


        userInfoDisplay(profileImageView,fullname,User_phone,User_address);
        changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();


                }else{
                    updateonlyuserinfo();

                }
            }
        });
    }

    private void updateonlyuserinfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullname.getText().toString());
        userMap.put("address",User_address.getText().toString());
        userMap.put("phoneOrder",User_phone.getText().toString());
        ref.child(Prevalent.onlineusers.getPhone()).updateChildren(userMap);
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);

        }else{
            Toast.makeText(SettingsActivity.this,"Error,Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullname.getText().toString())){
            Toast.makeText(this, "Name is Mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(User_address.getText().toString())){
            Toast.makeText(this, "Address is Mandatory", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(User_phone.getText().toString())){
            Toast.makeText(this, "Phone Number is Mandatory", Toast.LENGTH_SHORT).show();
        }else if (checker.equals("clicked")){
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("please wait,updating account's information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri!=null){
            final StorageReference fileRef=storageProfilePictureRef.child(Prevalent.onlineusers.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }) .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",fullname.getText().toString());
                        userMap.put("address",User_address.getText().toString());
                        userMap.put("phoneOrder",User_phone.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.onlineusers.getPhone()).updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this,"Error.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullname, final EditText user_phone, final EditText user_address) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.onlineusers.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullname.setText(name);
                        user_phone.setText(phone);
                        user_address.setText(address);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
