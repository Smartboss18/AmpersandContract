package com.example.shubham.ampersandcontract;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    RequestQueue queue;
    TextView fullName, role, phonenumber, email;
    String userID, GETUrl, userEmail, userFirstname, userLastname, userPhonenumber, userRole, userLinkedIn, userTwitter, userProfilePhoto;
    SharedPreferences sharedPreferences;
    CircularImageView twitter, linkedIn, circularImageView;
    Intent httpIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        queue = Volley.newRequestQueue(this);
        getDetails();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "ResultNotFound", Toast.LENGTH_SHORT).show();
            }else {
                try {
                   Log.i("SIGNINN", String.valueOf(result));
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void getDetails(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        userID = sharedPreferences.getString("id", "");
        Log.i("Responseee1", userID);

        GETUrl = "https://ampersand-contact-exchange-api.herokuapp.com/api/v1/profile/"+ userID;
        Log.i("Responseee2", GETUrl);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, GETUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.i("Responseee3", response);

                        fullName = findViewById(R.id.textFullname);
                        role = findViewById(R.id.textRole);
                        phonenumber = findViewById(R.id.textPhone);
                        email = findViewById(R.id.textEmail);
                        twitter = findViewById(R.id.circularTwitter);
                        linkedIn = findViewById(R.id.circularLinkedIn);
                        circularImageView = findViewById(R.id.circularProfilePhoto);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String userDetails = jsonObject.getString("user");
                            Log.i("Responseee3R", userDetails);

                            JSONObject jsonObject1 = new JSONObject(userDetails);
                            userEmail = jsonObject1.getString("email");
                            userFirstname = jsonObject1.getString("firstName");
                            userLastname = jsonObject1.getString("lastName");
                            userPhonenumber = jsonObject1.getString("phoneNumber");
                            userRole = jsonObject1.getString("role");
                            userLinkedIn = jsonObject1.getString("linkedIn");
                            userTwitter = jsonObject1.getString("twitter");

                            fullName.setText(userFirstname + " " + userLastname);
                            role.setText(userRole);
                            phonenumber.setText(userPhonenumber);
                            email.setText(userEmail);

                            twitter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    httpIntent = new Intent(Intent.ACTION_VIEW);
                                    httpIntent.setData(Uri.parse("http://twitter.com/" + userTwitter));
                                    startActivity(httpIntent);
                                }
                            });

                            linkedIn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    httpIntent = new Intent(Intent.ACTION_VIEW);
                                    httpIntent.setData(Uri.parse("https://www.linkedin.com/" + userLinkedIn));
                                    startActivity(httpIntent);
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Responseee3E", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Responseee4", "error => "+error.getLocalizedMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  header = new HashMap<String, String>();
                header.put( "Content-Type", "application/json");
                header.put("Accept", "application/json");
                header.put("x-access-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1Yzg2MjNhMzdiNDU3OTAwMDRhMTZhNjUiLCJpYXQiOjE1NTIyOTQ4ODd9.eMXlcE4e_5N2fSxrQaeYJyGCzBnhL_BeenaroWsaZ9s");
                return header;
            }
        };
        queue.add(postRequest);
    }

}


