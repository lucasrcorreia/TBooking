package rolu18oy.ju.se.layoutapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import rolu18oy.ju.se.layoutapp.R;
import rolu18oy.ju.se.layoutapp.SaveSharedPreference;
import rolu18oy.ju.se.layoutapp.r_NavigationActivity;


public class login_nav_restaurant_fragment extends Fragment {

    View view;

    EditText restEmail, restPassword;
    Button restLogin;
    FirebaseAuth mAuth;

    public void init(){
        mAuth = FirebaseAuth.getInstance();
        restEmail = (EditText) view.findViewById(R.id.restEmail);
        restPassword = (EditText) view.findViewById(R.id.restPassword);
        restLogin = (Button) view.findViewById(R.id.restLogin);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_nav_restaurant_fragment, null);
        init();

        restLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(restEmail.getText().toString(),restPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SaveSharedPreference.setLoggedIn(getContext(), true, restEmail.getText().toString());
                            Intent LOGIN = new Intent(getActivity(), r_NavigationActivity.class);
                            getActivity().startActivity(LOGIN);
                        }
                        else {
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }

}
