package com.example.finder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class select extends AppCompatActivity {
    TextView textView;
    Button find;
    Button help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        textView = (TextView) findViewById(R.id.textView);
        find = (Button) findViewById(R.id.btn3);
        help = (Button) findViewById(R.id.btn4);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(select.this,logIn.class);
                startActivity(myIntent);
            }
        });
    }
}
