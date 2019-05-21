package com.example.shubham.ampersandcontract;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText email, password;
    Button signIn;
    ProgressBar progressBar;
    String emailInput, passwordInput;
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        signIn = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressBar2);
        errorMessage = findViewById(R.id.errorMessage);

        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignIn();
            }
        });
    }

    public void userSignIn(){
        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();

        if (TextUtils.isEmpty(emailInput)){
            Toast.makeText(this, "Enter Email Address!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(passwordInput)){
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                                startActivity(intent);
                            }else {
                                errorMessage.setText(task.getException().getMessage());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }
}
