package rolu18oy.ju.se.layoutapp;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.Model.Upload;

public class r_CreateAccountActivity extends AppCompatActivity{


    private static final int PICK_IMAGE_REQUEST_PROFILE = 1;
    private static final int PICK_IMAGE_REQUEST_MENU = 1;

    Restaurant restaurant;

    Button ButtonCreateAccount;
    private Button mButtonImgRestMenu;
    private Button mButtonImgRestProfile;
    private ImageView viewImgRestProfile;
    private ImageView viewImgRestMenu;
    private Uri mImageUriProfile;
    private Uri mImageUriMenu;

    private EditText edtRestEmail;
    private EditText edtRestName;
    private EditText edtRestPassword;
    private EditText edtrestDescription;

    private ProgressBar mProgressBar;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    FirebaseDatabase database;
    DatabaseReference restaurants;
    FirebaseAuth mAuth;





    public void init(){

        database = FirebaseDatabase.getInstance();
        restaurants = database.getReference("Restaurants");

        ButtonCreateAccount = findViewById(R.id.btnCreateAccount);

        mButtonImgRestProfile = findViewById(R.id.restPicture);
        mButtonImgRestMenu = findViewById(R.id.restMenu);
        viewImgRestProfile = findViewById(R.id.imgRestPicture);
        viewImgRestMenu = findViewById(R.id.imgRestMenu);

        edtRestEmail = findViewById(R.id.edtRestEmail);
        edtRestName= findViewById(R.id.edtRestName);
        edtRestPassword = findViewById(R.id.edtRestPassword);
        edtrestDescription = findViewById(R.id.edtrestDescription);

        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mAuth = FirebaseAuth.getInstance();

        /*
        mButtonImgRestMenu.setOnClickListener(this);
        mButtonImgRestProfile.setOnClickListener(this);
        */
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_create_account_activity);

        init();

        mButtonImgRestProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooserForProfile();
            }
        });
        mButtonImgRestMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooserForMenu();
            }
        });
        ButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(r_CreateAccountActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    if(!validateForm()){
                        return;
                    }
                    restaurant = new Restaurant(edtRestEmail.getText().toString().replace(".",","),
                            edtRestName.getText().toString(),
                            edtRestPassword.getText().toString(),
                            edtrestDescription.getText().toString());

                    uploadFile(mImageUriProfile);
                    uploadFile(mImageUriMenu);

                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true, restaurant.getPassword().replace(",","."));
                    createRestAccount();
                    Intent intent = new Intent(r_CreateAccountActivity.this, r_NavigationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    public void createRestAccount(){
        mAuth.createUserWithEmailAndPassword(edtRestEmail.getText().toString(), edtRestPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        restaurants.child(restaurant.getRestaurantEmail()).setValue(restaurant);
                    }
                });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri mImageUri){

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    }, 500);
                    Upload upload = new Upload(restaurant.getRestaurantName().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            });
        }
        else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }


    private void openFileChooserForProfile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_PROFILE);
    }
    private void openFileChooserForMenu(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_MENU);
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mButtonImgRestProfile:
                openFileChooserForProfile();
                break;
            case R.id.mButtonImgRestMenu:
                openFileChooserForMenu();
                break;
        }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null && data.getData() != null){
            if(requestCode == PICK_IMAGE_REQUEST_PROFILE){
                mImageUriProfile = data.getData();
                viewImgRestProfile.setImageURI(mImageUriProfile);

                //Picasso.with(this).load(mImageUriProfile).into(viewImgRestProfile);
            }

            if(requestCode == PICK_IMAGE_REQUEST_MENU){
                mImageUriMenu = data.getData();
                viewImgRestMenu.setImageURI(mImageUriMenu);
            }

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = edtRestEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtRestEmail.setError("Required.");
            valid = false;
        } else {
            edtRestEmail.setError(null);
        }

        String password = edtRestPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtRestPassword.setError("Required.");
            valid = false;
        } else {
            edtRestPassword.setError(null);
        }
        return valid;
    }
}