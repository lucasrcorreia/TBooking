package rolu18oy.ju.se.layoutapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rolu18oy.ju.se.layoutapp.Model.User;
import rolu18oy.ju.se.layoutapp.NavigationActivity;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class createacc_nav_user_fragment extends Fragment {
    View view;

    FirebaseDatabase database;
    DatabaseReference users;
    FirebaseAuth mAuth;

    EditText edtPassword, edtMail, edtfirstname , edtlastname;
    Button btnCreateAccount;

    User user;

    public void init(){

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();


        edtMail = (EditText) view.findViewById(R.id.username);
        edtPassword = (EditText) view.findViewById(R.id.password);
        edtfirstname = (EditText) view.findViewById(R.id.first_name);
        edtlastname = (EditText) view.findViewById(R.id.last_name);

        btnCreateAccount = (Button) view.findViewById(R.id.signup);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.createacc_nav_user_fragment, null);

        init();


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    return;
                }
                user = new User(edtfirstname.getText().toString(),
                        edtlastname.getText().toString(),
                        edtMail.getText().toString().replace(".",","),
                        edtPassword.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(user.getEmail()).exists()){

                            Toast.makeText(getActivity(), "The Email already exits \n Email ", Toast.LENGTH_SHORT).show();
                        }

                        else{

                            SaveSharedPreference.setLoggedIn(getContext(), true, user.getEmail().replace(",","."),"user");
                            createUserAccount();
                            Intent intent = new Intent(getActivity(), NavigationActivity.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        return view;
    }
    private void createUserAccount(){
        mAuth.createUserWithEmailAndPassword(edtMail.getText().toString(), edtPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        users.child(user.getEmail().toLowerCase()).setValue(user);
                    }
                });
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = edtMail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtMail.setError("Required.");
            valid = false;
        } else {
            edtMail.setError(null);
        }

        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Required.");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        return valid;
    }
}
