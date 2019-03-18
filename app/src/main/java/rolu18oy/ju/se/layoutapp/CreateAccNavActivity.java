package rolu18oy.ju.se.layoutapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import rolu18oy.ju.se.layoutapp.Fragments.createacc_nav_restaurant_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.createacc_nav_user_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.login_nav_restaurant_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.login_nav_user_fragment;

public class CreateAccNavActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createacc_nav_activity);

        loadFragment(new createacc_nav_user_fragment());

        BottomNavigationView navigation = findViewById(R.id.navigation_login);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    public void startForgotPassAct (View view){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void startLoginActivity (View view){
        Intent intent = new Intent(this, LoginNavActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_userlogin:
                fragment = new createacc_nav_user_fragment();
                break;

            case R.id.navigation_restaurantslogin:
                fragment = new createacc_nav_restaurant_fragment();
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
