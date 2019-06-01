package com.example.shubham.ampersandcontract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(),LaunchPage.class);
            startActivity(intent);
        }
    }
}
