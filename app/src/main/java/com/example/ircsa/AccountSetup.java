package com.example.ircsa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class AccountSetup extends AppCompatActivity {
    public void onBackPressed(){
        super.onBackPressed();
        finish();

    }

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private EditText setupName;
    private Button setupBtn;
    private ProgressBar setupProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        //setting up image permissions
        setupImage = findViewById(R.id.setup_image);
        setupName = findViewById(R.id.setup_name);
        setupBtn = findViewById(R.id.setup_btn);
        setupProgress = findViewById(R.id.setup_progress);

        setupProgress.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

                        mainImageURI = Uri.parse(image);

                        setupName.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_profilepic2);
                        //Glide.with(AccountSetup.this).setDefaultRequestOptions(placeholderRequest).load(image).dontAnimate().into(setupImage);
                        Glide.with(AccountSetup.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);
//                        Log.i("check",image);
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(AccountSetup.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                setupProgress.setVisibility(View.INVISIBLE);
                setupBtn.setEnabled(true);

            }
        });



                //using Realtime Database
     /*   FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userdata = database.getReference("Users/"+user_id);

        userdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("name").getValue();
                String imgurl = (String) dataSnapshot.child("Image URL").getValue();

                setupname.setText(username);

                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.default_profilepic2);
                Glide.with(AccountSetup.this).setDefaultRequestOptions(placeholderRequest).load(imgurl).into(setupImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = setupName.getText().toString();
                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(AccountSetup.this, "Enter your Name", Toast.LENGTH_LONG).show();
                }
                else if(mainImageURI == null)
                {
                    Toast.makeText(AccountSetup.this, "Upload profile picture", Toast.LENGTH_LONG).show();
                }

                else if(!TextUtils.isEmpty(username) && mainImageURI != null){

                    setupProgress.setVisibility(View.VISIBLE);
                    if (isChanged) {

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(AccountSetup.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();

                        final UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_images");

                                if (task.isSuccessful()) {

                                    storageRef.child(user_id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String image = uri.toString();

                                            Map<String, String> userMap = new HashMap<>();
                                            userMap.put("name", username);
                                            userMap.put("image",image);

                                            firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){

                                                        Toast.makeText(AccountSetup.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                                                        Intent mainIntent = new Intent(AccountSetup.this, MainActivity.class);
                                                        startActivity(mainIntent);
                                                        finish();

                                                    } else {

                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(AccountSetup.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                                                    }

                                                    setupProgress.setVisibility(View.INVISIBLE);

                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });





                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(AccountSetup.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                                    setupProgress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });

                    } else {

                        storeFirestore(null, username);

                    }

                }

            }

        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(AccountSetup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(AccountSetup.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(AccountSetup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });
    }


    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name) {

        Uri download_uri;

                if(task != null) {

                    final String string_uri = task.getResult().getStorage().getDownloadUrl().toString();
                    Log.i("qwerty",string_uri);
                    download_uri = Uri.parse(string_uri);

                } else {

                    download_uri = mainImageURI;

                }


        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(AccountSetup.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(AccountSetup.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(AccountSetup.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

                setupProgress.setVisibility(View.INVISIBLE);

            }
        });


    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(AccountSetup.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
}
