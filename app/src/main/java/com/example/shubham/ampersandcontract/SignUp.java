package com.example.shubham.ampersandcontract;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    String firstName = "";
    String lastname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        email = findViewById(R.id.passwordLogin);
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

        String fullNameText = fullname.getText().toString();
        Log.i("NAMEE", fullNameText);

        if (fullNameText.split("\\w+").length>1){
            lastname = fullNameText.substring(fullNameText.lastIndexOf(" ")+1);
            firstName = fullNameText.substring(0, fullNameText.lastIndexOf(' '));

            Log.i("NAMEE", lastname);
            Log.i("NAMEE", firstName);
        }else {
            firstName = fullNameText;
        }

        if (fullname.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Full Name not entered", Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Phone Number not entered", Toast.LENGTH_SHORT).show();
        }else if (role.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Role not entered", Toast.LENGTH_SHORT).show();
        }else if (twitter.getText().toString().isEmpty()){
            Toast.makeText(SignUp.this, "Please enter Twitter Handle", Toast.LENGTH_SHORT).show();
        }else if (password.getText().toString().length()<6){
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
        } else{
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

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String details = jsonObject.getString("user");

                            Log.i("APIII", details);

                            JSONArray array = new JSONArray(details);
                            for (int i = 0; i<array.length(); i++){
                                JSONObject jsonPart = array.getJSONObject(i);
                                String email = jsonPart.getString("_id");

                                Log.i("emaill", email);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("APIII", e.getMessage().toString());
                        }

                        Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                        startActivity(intent);
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
                params.put("firstName", firstName);
                params.put("lastName", lastname);
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









//                        Log.i("APII", "Registered");
//                        Log.i("APII", response.toString());
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            JSONArray jsonArray = jsonResponse.getJSONArray("user");
//
//                            for (int i = 0; i<jsonArray.length(); i++){
//                                JSONObject dataObject = jsonArray.getJSONObject(i);
//                                String email = dataObject.getString("email");
//                                Toast.makeText(SignUp.this, email, Toast.LENGTH_SHORT).show();
//                                Log.i("APII", email);
//                            }
//
//                        }catch (JSONException e) {
//                            e.printStackTrace();
//                        }


