package com.example.shubham.ampersandcontract;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText email, password;
    Button signIn;
    ProgressBar progressBar;
    String emailInput, passwordInput;
    TextView errorMessage, resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        signIn = findViewById(R.id.signIn);
        progressBar = findViewById(R.id.progressBar2);
        errorMessage = findViewById(R.id.errorMessage);
        resetPassword = findViewById(R.id.resetPassword);

        resetPassword.setPaintFlags(resetPassword.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);


        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignIn();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();
    }

    public void userSignIn(){

        if (email.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Email Address!", Toast.LENGTH_SHORT).show();
        }else if (password.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                postToAPI();
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

    public  void postToAPI(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://ampersand-contact-exchange-api.herokuapp.com/api/v1/login";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(getApplicationContext(), "POSTED", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email", emailInput);
                params.put("password", passwordInput);
                return params;
            }
        };
        queue.add(postRequest);

        Log.i("POSTEDDD", "WORKS");
    }

    public void resetPassword(){
        if (email.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter Registered Email!", Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Reset Link Sent To Email", Toast.LENGTH_SHORT).show();
                            }else {
                                errorMessage.setText(task.getException().getMessage());
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }

    }
}
