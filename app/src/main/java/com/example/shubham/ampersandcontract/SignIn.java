package com.example.shubham.ampersandcontract;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText email, password;
    Button signIn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        signIn = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressBar2);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            
        }

    }
}
