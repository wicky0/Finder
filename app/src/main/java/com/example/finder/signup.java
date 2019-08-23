package com.example.finder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class signup extends AppCompatActivity {
    private static final String TAG = "signup";
    
    TextView textView, text;
    EditText names, number, email, city,password,confpass;
    CircleImageView imageView;
    ImageButton icon;
    Button register;
    CountryCodePicker countryCodePicker;
    Uri muUri;
    private String stringImage;
    private static final int CAMERA_PIC_REQUEST = 704;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (CircleImageView) findViewById(R.id.image);
        icon = (ImageButton) findViewById(R.id.icon);
        names = (EditText) findViewById(R.id.names);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        number = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        city = (EditText) findViewById(R.id.city);
        password = (EditText) findViewById(R.id.password);
        confpass = (EditText) findViewById(R.id.confPass);
        register = (Button) findViewById(R.id.register);
        text = (TextView) findViewById(R.id.login);
        countryCodePicker.registerCarrierNumberEditText(number);

        //return to login screen
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(signup.this, logIn.class);
                startActivity(myIntent);
                finish();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = names.getText().toString();
                String phoneNumber = countryCodePicker.getFullNumberWithPlus();
                String cit = city.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String conf = confpass.getText().toString();

                Intent myIntent = new Intent(signup.this, MyIntentService.class);
                myIntent.putExtra("images", stringImage);
                myIntent.putExtra("names",name);
                myIntent.putExtra("countryCodePicker",phoneNumber);
                myIntent.putExtra("city",cit);
                myIntent.putExtra("email",mail);
                myIntent.putExtra("password",pass);
                myIntent.putExtra("confpass",conf);
                startActivity(myIntent);
                final ProgressDialog progressDialog = new ProgressDialog(signup.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            CircleImageView imageView = (CircleImageView) findViewById(R.id.image);
            imageView.setImageBitmap(image);

            muUri = getImageUri(this,image);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("pictures");
            final StorageReference imagesRef = storageRef.child(muUri .getLastPathSegment());
            UploadTask uploadTask = imagesRef.putFile(muUri );
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imagesRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        stringImage = downloadUri.toString();
                        Log.d("SelectedImage","This is the selected image " + stringImage);

                    } else
                    {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
    private Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }

    public boolean validate() {
        boolean valid = true;
        String name = names.getText().toString();
        String phoneNumber = countryCodePicker.getFullNumberWithPlus();
        String cit = city.getText().toString();
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        String conf = confpass.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            names.setError("at least 3 characters");
            valid = false;
        } else {
            names.setError(null);
        }
        if (phoneNumber.isEmpty() || phoneNumber.length() < 15) {
            number.setError("enter your Phone No.");
            valid = false;
        }else {
            number.setError(null);
        }
        if (cit.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("enter valid email address");
            valid = false;
        }else {
            email.setError(null);
        }
        if (pass.isEmpty() || pass.length() <8){
            password.setError("weak password at least 8 characters");
            valid = false;
        } else {
            password.setError(null);
        }
        if (conf.isEmpty() ||conf.length() <8){
            confpass.setError("wrong!!");
            valid = false;
        } else {
            confpass.setError(null);
        }
        return valid;
    }
}