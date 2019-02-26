package rolu18oy.ju.se.layoutapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        startActivity(intent);

    }
}
