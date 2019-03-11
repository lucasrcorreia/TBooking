package rolu18oy.ju.se.layoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import rolu18oy.ju.se.layoutapp.Fragments.Booked_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.Restaurants_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.User_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.login_nav_restaurant_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.login_nav_user_fragment;

public class LoginNavActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_nav_activity);

        loadFragment(new login_nav_user_fragment());

        BottomNavigationView navigation = findViewById(R.id.navigation_login);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    public void startCreateAccAct (View view){
        Intent intent = new Intent(this, CreateAccNavActivity.class);
        startActivity(intent);
    }
    public void startForgotPassAct (View view){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    public void r_startCreateAccAct (View view){
        Intent intent = new Intent(this, CreateAccNavActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_userlogin:
                fragment = new login_nav_user_fragment();
                break;

            case R.id.navigation_restaurantslogin:
                fragment = new login_nav_restaurant_fragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_login, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
