package rolu18oy.ju.se.layoutapp;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    EditText edtPassword, edtMail, edtfirstname , edtlastname;
    Button btnCreateAccount;

    public void init(){
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtMail = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtfirstname = (EditText) findViewById(R.id.first_name);
        edtlastname = (EditText) findViewById(R.id.last_name);

        btnCreateAccount = (Button) findViewById(R.id.signup);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("check", edtMail.getText().toString());
                System.out.print(edtMail.getText().toString());
                final User user = new User(edtfirstname.getText().toString(),
                        edtlastname.getText().toString(),
                        edtMail.getText().toString(),
                        edtPassword.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getEmail()).exists()){
                            Toast.makeText(CreateAccountActivity.this, "The Email already exits \n Email ", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            users.child(user.getEmail()).setValue(user);
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
}
