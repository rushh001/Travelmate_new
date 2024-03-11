package com.example.travelmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {

    String text="Travelmate..";
    TextView textView;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
     textView=findViewById(R.id.textview);
     initialize(text);


    }

    private void initialize(String passes_text) {
        if(i<passes_text.length())
        {
            String fetch=text.substring(0,i);
            textView.setText(fetch);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    i++;
                    initialize(passes_text);

                    if (i == text.length())
                    {  Intent intent = new Intent(Splashscreen.this, SignUp.class);
                    startActivity(intent);
                    finish();
                }
            }
            },150);


        }
    }
}