package com.example.shubham.ampersandcontract;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private static  final int SELECT_PICTURE = 1;

    Button register;
    EditText email, password, fullname, phoneNumber, role, twitter, linkedIn;
    TextView instruction;
    ProgressBar progressBar;
    ImageButton profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        profilepic = findViewById(R.id.profilePic);
        instruction = findViewById(R.id.instruction);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register();
                }
            });

            profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPicture();
                }
            });

    }


    public void register(){

        if (fullname.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Full Name not entered", Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Phone Number not entered", Toast.LENGTH_SHORT).show();
        }else if (role.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Role not entered", Toast.LENGTH_SHORT).show();
        }else if (twitter.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Please enter Twitter Handle", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            try {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                postToApi();
            }catch (Exception e){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignUp.this, "Empty Fields Present", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void postToApi(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://ampersand-contact-exchange-api.herokuapp.com/api/v1/register";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(SignUp.this, "POSTED", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(SignUp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("email", email.getText().toString() );
                params.put("password", password.getText().toString());
                params.put("fullName", fullname.getText().toString());
                params.put("phoneNumber", phoneNumber.getText().toString());
                params.put("twitter", twitter.getText().toString());
                params.put("linkedIn", linkedIn.getText().toString());
                params.put("role", role.getText().toString());

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void selectPicture(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            instruction.setText("Change Photo");
            instruction.setTextColor(Color.WHITE);
            profilepic.setImageBitmap(bitmap);
        }
    }



}
