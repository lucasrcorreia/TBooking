package rolu18oy.ju.se.layoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import rolu18oy.ju.se.layoutapp.Fragments.Booked_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.Restaurants_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.User_fragment;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String email;
    SharedPreferences sp;
    String Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);

        //SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "no");
        //loading the default fragment
        loadFragment(new Restaurants_fragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_restaurants:
                fragment = new Restaurants_fragment();
                break;

            case R.id.navigation_booked:
                fragment = new Booked_fragment();
                break;

            case R.id.navigation_user:
                fragment = new User_fragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    public void startLoginAct (View view){
        Intent intent = new Intent(this, LoginNavActivity.class);
        startActivity(intent);

    }

    public void LogOutstartLoginAct (View view){
        Intent intent = new Intent(this, LoginNavActivity.class);
        startActivity(intent);

    }

}
