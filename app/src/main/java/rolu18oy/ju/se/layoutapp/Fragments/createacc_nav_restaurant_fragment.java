package rolu18oy.ju.se.layoutapp.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;
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

    private StorageTask mUploadProfile;
    private StorageTask mUploadMenu;

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

                if (mUploadProfile != null && mUploadProfile.isInProgress() || mUploadMenu != null && mUploadMenu.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    if(!validateForm()){
                        return;
                    }

                    uploadFile(mImageUriProfile, mImageUriMenu);

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

    private void uploadFile(Uri mImageUriProfile, Uri mImageUriMenu){

        if (mImageUriProfile != null || mImageUriMenu != null) {

            final StorageReference fileReferenceProfile = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUriProfile));


            mUploadProfile = fileReferenceProfile.putFile(mImageUriProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReferenceProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);

                                }
                            }, 500);
                            ImageProgileUri = uri.toString();
                            restaurant.setRestaurantProfile(uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            final StorageReference fileReferenceMenu = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUriMenu));

            mUploadMenu = fileReferenceMenu.putFile(mImageUriMenu).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReferenceMenu.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);

                                }
                            }, 500);

                            restaurant.setRestaurantMenu(uri.toString());
                            ImageMenuUri = uri.toString();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }
        else{
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
        }


        restaurant = new Restaurant(edtRestEmail.getText().toString().replace(".",","),
                edtRestName.getText().toString(),
                edtRestPassword.getText().toString(),
                edtrestDescription.getText().toString(),ImageProgileUri,ImageMenuUri);

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
