package com.example.finder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class verify_code extends AppCompatActivity {
    EditText verify;
    Button send;
    int ver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        verify = (EditText) findViewById(R.id.verify);
        send = (Button) findViewById(R.id.btn2);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(verify_code.this,MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
