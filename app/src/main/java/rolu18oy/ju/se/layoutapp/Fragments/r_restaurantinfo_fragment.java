package rolu18oy.ju.se.layoutapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rolu18oy.ju.se.layoutapp.ForgotPasswordActivity;
import rolu18oy.ju.se.layoutapp.LoginNavActivity;
import rolu18oy.ju.se.layoutapp.Model.Restaurant;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class r_restaurantinfo_fragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference restaurants;
    SharedPreferences sp;

    TextView RestName;
    TextView RestDescription;
    TextView RestEmail;

    Button btlogout;
    Button btdelete_acc;
    Button btnChangePass;
    Button btsignin;
    Button ChangeRestmenu;
    Button ChangeRestdesc;
    Button ChangeRestname;

    String Email;
    String RestaurantName;
    String PasswordConfirm;

    Restaurant restaurant;

    public void refreshFragment(){
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("r_restaurantinfo_fragment");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    public void init(){
        sp = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();

        RestName = view.findViewById(R.id.RestName);
        RestDescription = view.findViewById(R.id.RestDescription);
        RestEmail = view.findViewById(R.id.RestEmail);

        btlogout = view.findViewById(R.id.btlogout);
        btdelete_acc = view.findViewById(R.id.btdelete_acc);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btsignin = view.findViewById(R.id.btsignin);
        ChangeRestmenu = view.findViewById(R.id.ChangeRestmenu);
        ChangeRestdesc = view.findViewById(R.id.ChangeRestdesc);
        ChangeRestname = view.findViewById(R.id.ChangeRestname);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.r_restaurantinfo_fragment, null);
        init();

        if(SaveSharedPreference.getLoggedStatus(getContext())) {
            Email = SaveSharedPreference.getLoggedEmail(getContext());
            restaurants = database.getReference("Restaurants").child(Email.replace(".",","));
            retrieveRestaurantInfo();
            RestEmail.setText(Email);
            btsignin.setVisibility(View.INVISIBLE);
        }
        else {
            btlogout.setVisibility(View.INVISIBLE);
            btdelete_acc.setVisibility(View.INVISIBLE);
            btnChangePass.setVisibility(View.INVISIBLE);
            ChangeRestmenu.setVisibility(View.INVISIBLE);
            ChangeRestdesc.setVisibility(View.INVISIBLE);
            ChangeRestname.setVisibility(View.INVISIBLE);

        }

        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshNavigationActivity();
            }
        });

        btdelete_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPassword(Email);

            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        ChangeRestname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRestName();
                refreshFragment();

            }
        });
        ChangeRestdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRestDescription();
                refreshFragment();
            }
        });

        return view;
    }
    public void RefreshNavigationActivity(){
        SaveSharedPreference.setLoggedIn(getContext(), false, "","");
        Intent intent = new Intent(getActivity(), LoginNavActivity.class);
        startActivity(intent);
    }

    public void DeleteAccount(String email, String password){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            restaurants.removeValue();
                            RefreshNavigationActivity();
                            Log.d("Account deleted", "User account deleted!");
                        }
                    }
                });
            }
        });
    }
    public void confirmPassword(final String email){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Comfirm with your password");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);


        builder.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PasswordConfirm = input.getText().toString();
                DeleteAccount(email,PasswordConfirm);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void retrieveRestaurantInfo(){
        restaurants.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurant = dataSnapshot.getValue(Restaurant.class);
                RestName.setText(restaurant.getRestaurantName());
                RestDescription.setText(restaurant.getDescription());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void changeRestName(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Input your new name");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("Change Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restaurants.child("RestaurantName").setValue(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void changeRestDescription(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Input your new Description");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Change Description", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restaurants.child("Description").setValue(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}