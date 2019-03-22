package rolu18oy.ju.se.layoutapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;

import rolu18oy.ju.se.layoutapp.Fragments.Booked_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.Rest_description_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.Restaurants_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.User_fragment;
import rolu18oy.ju.se.layoutapp.Model.DatePickerFragment;
import rolu18oy.ju.se.layoutapp.Model.Restaurant;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    String email;
    SharedPreferences sp;
    String Login;
    String tag;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);

        //SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "","");
        //loading the default fragment
        tag = "rest_fragment";
        loadFragment(new Restaurants_fragment(), tag);

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
                tag="rest_fragment";
                break;

            case R.id.navigation_booked:
                fragment = new Booked_fragment();
                tag="booked_fragment";
                break;

            case R.id.navigation_user:
                fragment = new User_fragment();
                tag="user_fragment";
                break;
        }

        return loadFragment(fragment, tag);
    }

    private boolean loadFragment(Fragment fragment, String tag) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment,tag)
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

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("rest_fragment");
        if(fragment!=null && fragment.isVisible()){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }else{
            Intent a = new Intent(this, NavigationActivity.class);
            startActivity(a);
        }

        //super.onBackPressed();
    }
}
