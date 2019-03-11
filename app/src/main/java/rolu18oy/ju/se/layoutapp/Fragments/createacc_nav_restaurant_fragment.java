package rolu18oy.ju.se.layoutapp.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;
import rolu18oy.ju.se.layoutapp.r_CreateAccountActivity;
import rolu18oy.ju.se.layoutapp.r_NavigationActivity;


public class createacc_nav_restaurant_fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST_PROFILE = 1;
    private static final int PICK_IMAGE_REQUEST_MENU = 2;

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
    //private String RestProfilePicture;
    //private String RestMenuPicture;

    private ProgressBar mProgressBar;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    FirebaseDatabase database;
    DatabaseReference restaurants;
    FirebaseAuth mAuth;



    String ImageProgileUri;
    String ImageMenuUri;


    View view;

    public void init(){

        database = FirebaseDatabase.getInstance();
        restaurants = database.getReference("Restaurants");

        ButtonCreateAccount = view.findViewById(R.id.btnCreateAccount);

        mButtonImgRestProfile = view.findViewById(R.id.restPicture);
        mButtonImgRestMenu = view.findViewById(R.id.restMenu);
        viewImgRestProfile = view.findViewById(R.id.imgRestPicture);
        viewImgRestMenu = view.findViewById(R.id.imgRestMenu);

        edtRestEmail = view.findViewById(R.id.edtRestEmail);
        edtRestName= view.findViewById(R.id.edtRestName);
        edtRestPassword = view.findViewById(R.id.edtRestPassword);
        edtrestDescription = view.findViewById(R.id.edtrestDescription);

        mProgressBar = view.findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mAuth = FirebaseAuth.getInstance();




    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.createacc_nav_restaurant_fragment, null);


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
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    if(!validateForm()){
                        return;
                    }

                    uploadFile(mImageUriProfile, "ProfilePic");
                    uploadFile(mImageUriMenu, "MenuPic");

                    restaurant = new Restaurant(edtRestEmail.getText().toString().replace(".",","),
                            edtRestName.getText().toString(),
                            edtRestPassword.getText().toString(),
                            edtrestDescription.getText().toString());

                    SaveSharedPreference.setLoggedIn(getContext(), true, restaurant.getPassword().replace(",","."));
                    createRestAccount();
                    Intent intent = new Intent(getActivity(), r_NavigationActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
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
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(Uri mImageUri, final String ImageName){

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

                    if(ImageName == "ProfilePic"){
                        //ImageProgileUri = ;
                        restaurant.setRestaurantProfile(taskSnapshot.getStorage().getDownloadUrl().toString());
                    }
                    if(ImageName == "MenuPic"){
                        restaurant.setRestaurantMenu(taskSnapshot.getStorage().getDownloadUrl().toString());
                        //ImageMenuUri = taskSnapshot.getStorage().getDownloadUrl().toString();
                    }

                    Upload upload = new Upload(restaurant.getRestaurantName().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);

                    //ImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();

                    //String ImageProgileUri;
                    //String ImageMenuUri;


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
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }
        //return ImageUrl;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null){

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
        if(password.length()<6){
            edtRestPassword.setError("Password More than 6 char");
        }
        return valid;
    }
}
