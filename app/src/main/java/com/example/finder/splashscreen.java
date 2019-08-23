package com.example.finder;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class splashscreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;
    CircleImageView imageView;
    TextView text;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        imageView = (CircleImageView) findViewById(R.id.circleImageView);
        text = (TextView) findViewById(R.id.text2);
        textView = (TextView) findViewById(R.id.text3);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(splashscreen.this, verify_code.class);
                splashscreen.this.startActivity(mainIntent);
                splashscreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
