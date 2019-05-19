package com.example.shubham.ampersandcontract;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button register;
    EditText email, password, fullname, phoneNumber, role, twitter, linkedIn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fullname = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        role = findViewById(R.id.role);
        twitter = findViewById(R.id.twitter);
        linkedIn = findViewById(R.id.linkedIn);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (fullname.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Full Name not entered", Toast.LENGTH_SHORT).show();
                    }else if (phoneNumber.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Phone Number not entered", Toast.LENGTH_SHORT).show();
                    }else if (role.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Role not entered", Toast.LENGTH_SHORT).show();
                    }else if (twitter.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Please enter Twitter Handle", Toast.LENGTH_SHORT).show();
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }catch (Exception e){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Empty Fields Present", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }



//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//    }
}
