package rolu18oy.ju.se.layoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import rolu18oy.ju.se.layoutapp.Fragments.create_table_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.r_bookedtables_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.r_freetables_fragment;
import rolu18oy.ju.se.layoutapp.Fragments.r_restaurantinfo_fragment;

public class r_NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_navigation_activity);

        //SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "","");
        //loading the default fragment
        tag = "r_freetables_fragment";
        loadFragment(new r_freetables_fragment(), tag);

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.R_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_freetables:
                fragment = new r_freetables_fragment();
                tag = "r_freetables_fragment";
                break;

            case R.id.navigation_bookedtables:
                fragment = new r_bookedtables_fragment();
                tag = "r_bookedtables_fragment";
                break;

            case R.id.navigation_restaurantinfo:
                fragment = new r_restaurantinfo_fragment();
                tag = "r_restaurantinfo_fragment";
                break;
        }

        return loadFragment(fragment, tag);
    }

    private boolean loadFragment(Fragment fragment, String tag) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.R_fragment_container, fragment, tag)
                    .commit();
            return true;
        }
        return false;
    }

    public void create_table_frag_start(View view) {
        loadFragment(new create_table_fragment(), "create_table_fragment");
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("r_freetables_fragment");
        if (fragment != null && fragment.isVisible()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Intent a = new Intent(this, NavigationActivity.class);
            startActivity(a);
        }

        //super.onBackPressed();
    }
}
