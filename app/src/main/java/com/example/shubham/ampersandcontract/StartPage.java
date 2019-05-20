package com.example.shubham.ampersandcontract;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StartPage extends AppCompatActivity {

    TextView register, login;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        login.setPaintFlags(login.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        register.setPaintFlags(register.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                register.setBackgroundColor(Color.parseColor("#9e9e9e"));
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                login.setBackgroundColor(Color.parseColor("#9e9e9e"));
                startActivity(intent);
            }
        });
    }
}