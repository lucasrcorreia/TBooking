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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import rolu18oy.ju.se.layoutapp.CreateAccountActivity;
import rolu18oy.ju.se.layoutapp.ForgotPasswordActivity;
import rolu18oy.ju.se.layoutapp.LoginActivity;
import rolu18oy.ju.se.layoutapp.Model.User;
import rolu18oy.ju.se.layoutapp.NavigationActivity;
import rolu18oy.ju.se.layoutapp.R;

import rolu18oy.ju.se.layoutapp.SaveSharedPreference;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference.*;


public class User_fragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference users;
    Button btnChangePass, logout, delete_acc, signin;
    SharedPreferences sp;
    View view;
    String Email;
    TextView Fullname, useremail;
    String PasswordConfirm;
    String FullN = "";

    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.user_fragments,null);
        init();


        if(SaveSharedPreference.getLoggedStatus(getContext())) {
            Email = SaveSharedPreference.getLoggedEmail(getContext());
            users = database.getReference("Users").child(Email.replace(".",","));
            retrieveUserFullName();
            useremail.setText(Email);
            signin.setVisibility(View.INVISIBLE);
        }
        else {
            Fullname.setVisibility(View.INVISIBLE);
            useremail.setVisibility(View.INVISIBLE);
            btnChangePass.setVisibility(View.INVISIBLE);
            delete_acc.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.INVISIBLE);
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshNavigationActivity();
            }
        });

        delete_acc.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }
    public void RefreshNavigationActivity(){
        SaveSharedPreference.setLoggedIn(getContext(), false, "");
        Intent intent = new Intent(getActivity(), NavigationActivity.class);
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
                            users.removeValue();
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
    public void init() {
        sp = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        btnChangePass = (Button) view.findViewById(R.id.btnChangePass);
        logout = (Button) view.findViewById(R.id.logout);
        delete_acc = (Button) view.findViewById(R.id.delete_acc);
        signin = (Button) view.findViewById(R.id.signin);
        Fullname = (TextView) view.findViewById(R.id.Fullname);
        useremail = (TextView) view.findViewById(R.id.useremail);

        database = FirebaseDatabase.getInstance();

    }

    public void retrieveUserFullName(){

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                Fullname.setText(user.getFirstName() +" "+ user.getLastName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
