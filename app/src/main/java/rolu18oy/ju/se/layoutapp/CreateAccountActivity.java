package rolu18oy.ju.se.layoutapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rolu18oy.ju.se.layoutapp.Model.User;

public class CreateAccountActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users;
    FirebaseAuth mAuth;

    EditText edtPassword, edtMail, edtfirstname , edtlastname;
    Button btnCreateAccount;

    User user;

    public void init(){
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        edtMail = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtfirstname = (EditText) findViewById(R.id.first_name);
        edtlastname = (EditText) findViewById(R.id.last_name);

        btnCreateAccount = (Button) findViewById(R.id.signup);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity);

        init();


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    return;
                }
                user = new User(edtfirstname.getText().toString(),
                        edtlastname.getText().toString(),
                        edtMail.getText().toString().replace(".",","),
                        edtPassword.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(user.getEmail()).exists()){

                            Toast.makeText(CreateAccountActivity.this, "The Email already exits \n Email ", Toast.LENGTH_SHORT).show();
                        }

                        else{

                            SaveSharedPreference.setLoggedIn(getApplicationContext(), true, user.getEmail().replace(",","."));
                            createAccount(user.getEmail(),user.getPassword());
                            Intent intent = new Intent(CreateAccountActivity.this, NavigationActivity.class);
                            startActivity(intent);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void createAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(edtMail.getText().toString(), edtPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        users.child(user.getEmail()).setValue(user);
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = edtMail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edtMail.setError("Required.");
            valid = false;
        } else {
            edtMail.setError(null);
        }

        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Required.");
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        return valid;
    }

    public boolean PasswordCheck(String password0, String password1){
        if(password0 == password1){
            return true;
        }
        return false;
    }
}
