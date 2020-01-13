package com.example.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProduct extends AppCompatActivity {
    private String CategoryName,Description,Price,Pname,savedate,savetime;
    private Button addnew;
    private EditText pname,pdescription,pprice;
    private ImageView prodimage;
    private static final int gallery_image=1;
    private Uri imageuri;
    private String ProductRandomKey,downloadImageUrl;  //to combine date and time
    private StorageReference ProductImageRef;
    private DatabaseReference Productsref,Sellersref;
    private ProgressDialog loadBar;
    private String sName,sAddress,sPhone,sEmail,sID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        CategoryName=getIntent().getExtras().get("category").toString();
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        Productsref=FirebaseDatabase.getInstance().getReference().child("Products");
        Sellersref=FirebaseDatabase.getInstance().getReference().child("Sellers");
        addnew=findViewById(R.id.add_new);
        pname=findViewById(R.id.product_name);
        pdescription=findViewById(R.id.product_description);
        pprice=findViewById(R.id.product_price);
        prodimage=findViewById(R.id.select_product_image);
        loadBar = new ProgressDialog(this);
        prodimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        Sellersref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    sName=dataSnapshot.child("name").getValue().toString();
                    sPhone=dataSnapshot.child("phone").getValue().toString();
                    sAddress=dataSnapshot.child("address").getValue().toString();
                    sEmail=dataSnapshot.child("email").getValue().toString();
                    sID=dataSnapshot.child("sid").getValue().toString();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void ValidateProductData() {
        Description=pdescription.getText().toString();
        Price=pprice.getText().toString();
        Pname=pname.getText().toString();
        if (imageuri==null){
            Toast.makeText(SellerAddNewProduct.this,"Product image is mandatory",Toast.LENGTH_LONG).show();

        }else if (TextUtils.isEmpty(Description)){
            Toast.makeText(SellerAddNewProduct.this,"please write description",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(Pname)){
            Toast.makeText(SellerAddNewProduct.this,"please write name of product",Toast.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(Price)){
            Toast.makeText(SellerAddNewProduct.this,"please write price of product",Toast.LENGTH_LONG).show();
        }else {
            StoreProductInfo();
        }

    }

    private void StoreProductInfo() {
        loadBar.setTitle("Adding Product");
        loadBar.setMessage("Please wait");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.show();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat presentdate=new SimpleDateFormat("MMM dd, yyyy");
        savedate=presentdate.format(calendar.getTime());
        SimpleDateFormat presenttime=new SimpleDateFormat("HH:mm:ss a");
        savetime=presenttime.format(calendar.getTime());
        ProductRandomKey= savedate + savetime;
        final StorageReference filepath=ProductImageRef.child(imageuri.getLastPathSegment() + ProductRandomKey + ".jpg");
        final UploadTask uploadTask=filepath.putFile(imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(SellerAddNewProduct.this,"Error:" + message,Toast.LENGTH_LONG).show();
                loadBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProduct.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();


                        }
                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(SellerAddNewProduct.this,"product image url saved",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                        }

                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object>productmap=new HashMap<>();
        productmap.put("pid",ProductRandomKey);
        productmap.put("date",savedate);
        productmap.put("time",savetime);
        productmap.put("description",Description);
        productmap.put("image",downloadImageUrl);
        productmap.put("category",CategoryName);
        productmap.put("price",Price);
        productmap.put("pname",Pname);

        productmap.put("sellerName",sName);
        productmap.put("sellerAddress",sAddress);
        productmap.put("sellerPhone",sPhone);
        productmap.put("sellerEmail",sEmail);
        productmap.put("sid",sID);
        productmap.put("productstate","Not Approved");
        Productsref.child(ProductRandomKey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent=new Intent(SellerAddNewProduct.this, SellerHomeActivity.class);
                    startActivity(intent);
                    loadBar.dismiss();
                    Toast.makeText(SellerAddNewProduct.this,"Product is added succesfully",Toast.LENGTH_SHORT).show();
                }else {
                    loadBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(SellerAddNewProduct.this,"Error:" + message,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void openGallery(){
        Intent galley=new Intent();
        galley.setAction(Intent.ACTION_GET_CONTENT);
        galley.setType("image/*");
        startActivityForResult(galley,gallery_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==gallery_image && resultCode==RESULT_OK && data!=null){
            imageuri=data.getData();
            prodimage.setImageURI(imageuri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
