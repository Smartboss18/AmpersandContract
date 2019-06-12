package com.example.shubham.ampersandcontract;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.karumi.dexter.Dexter.*;

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
    String id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String IMAGE_DIRECTORY = "/ampersand";
    private int GALLERY = 1, CAMERA = 2;
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        email = findViewById(R.id.passwordLogin);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fullname = findViewById(R.id.textFullname);
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
                    requestMultiplePermissions();
                    showPictureDialog();
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

                        Toast.makeText(SignUp.this, "POSTED", Toast.LENGTH_SHORT).show();
                        Log.i("resposnsee", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String userDetail = jsonObject.getString("user");
                            JSONObject jsonObject1 = new JSONObject(userDetail);
                            id = jsonObject1.getString("_id");

                            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            editor = sharedPreferences.edit();
                            editor.putString("id", id);
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("APIIIE", e.getMessage().toString());
                        }


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
                params.put("photo", getBase64String());

                return params;
            }
        };

        queue.add(postRequest);
    }

    private void  requestMultiplePermissions(){
        withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    profilepic.setImageBitmap(bitmap);
                    Log.i("PHOTOO1", path);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profilepic.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            String path = saveImage(thumbnail);
            Log.i("PHOTOO2", path);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profilepic.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            getBase64String();

            Log.i("StringPic", getBase64String().toString());
            Log.i("StringPic2", f.getAbsolutePath());

            return f.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    private String getBase64String() {

        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}