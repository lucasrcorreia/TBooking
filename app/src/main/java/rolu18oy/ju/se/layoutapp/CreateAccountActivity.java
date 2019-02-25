package rolu18oy.ju.se.layoutapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rolu18oy.ju.se.layoutapp.Model.User;

public class CreateAccountActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users;

    EditText edtPassword, edtMail;
    Button btnCreateAccount;

    private String userId;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final User user = new User(
                        edtPassword.getText().toString(),
                        edtMail.getText().toString().replace(".",","));

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(user.getEmail()).exists()){
                            Toast.makeText(CreateAccountActivity.this, "The Email already exits \n Email ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            users.child(user.getEmail()).setValue(user);
                            Toast.makeText(CreateAccountActivity.this,"Success Register!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public boolean PasswordCheck(String password0, String password1){
        if(password0 == password1){
            return true;
        }
        return false;
    }

    public void init(){
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtMail = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnCreateAccount = (Button) findViewById(R.id.signup);
    }

    private void createUser(String email, String password) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(email, password);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.getEmail() + ", " + user.getPassword());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }
}
