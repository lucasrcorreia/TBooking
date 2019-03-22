package rolu18oy.ju.se.layoutapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import rolu18oy.ju.se.layoutapp.NavigationActivity;
import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;


public class login_nav_user_fragment extends Fragment {

    View view;

    EditText userEmail, userPassword;
    Button UserLogin;
    FirebaseAuth Auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_nav_user_fragment, null);
        init();
        UserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateForm()){
                    return;
                }

                Auth.signInWithEmailAndPassword(userEmail.getText().toString().toLowerCase(),userPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SaveSharedPreference.setLoggedIn(getContext(), true, userEmail.getText().toString(),"user");
                                    Intent LOGIN = new Intent(getActivity(), NavigationActivity.class);
                                    getActivity().startActivity(LOGIN);
                                    Log.d("Logined In", "Email sent.");
                                }
                                else {
                                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    Log.e("FIALED  ", "Sign-in Failed: " + task.getException().getMessage());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIALED  ", "Sign-in Failed: " + e.getMessage());
                        Log.d("auther",userEmail.getText().toString()+" "+userPassword.getText().toString());
                        Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }

    public void init(){
        Auth = FirebaseAuth.getInstance();
        userEmail = (EditText) view.findViewById(R.id.userEmail);
        userPassword = (EditText) view.findViewById(R.id.userPassword);
        UserLogin = (Button) view.findViewById(R.id.userLogin);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = userEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userEmail.setError("Required.");
            valid = false;
        } else {
            userEmail.setError(null);
        }

        String password = userPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            userPassword.setError("Required.");
            valid = false;
        } else {
            userPassword.setError(null);
        }
        if(password.length()<6){
            userPassword.setError("Password has to be greater than 6");
            valid = false;
        }
        return valid;
    }
}
