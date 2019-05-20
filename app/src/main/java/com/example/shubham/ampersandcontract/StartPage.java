package com.example.shubham.ampersandcontract;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
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
                intent = new Intent(getApplicationContext(), SignUp.class);
                intent.putExtra("Name", "Register");
                register.setBackgroundColor(Color.parseColor("#9e9e9e"));
                startActivity(intent);
                CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) { }

                    @Override
                    public void onFinish() {
                        register.setBackgroundColor(Color.TRANSPARENT);
                    }
                }.start();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SignIn.class);
                intent.putExtra("Name", "LogIn");
                login.setBackgroundColor(Color.parseColor("#9e9e9e"));
                startActivity(intent);
                CountDownTimer countDownTimer = new CountDownTimer(1000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) { }

                    @Override
                    public void onFinish() {
                        login.setBackgroundColor(Color.TRANSPARENT);
                    }
                }.start();
            }
        });
    }
}
