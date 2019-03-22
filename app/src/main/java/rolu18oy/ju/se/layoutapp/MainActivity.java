package rolu18oy.ju.se.layoutapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SaveSharedPreference.getLoggedIdentification(getBaseContext()).equals("restaurant")){
            intent = new Intent(this, r_NavigationActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
