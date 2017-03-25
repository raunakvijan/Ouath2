package com.example.raunak.oauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void linkedin(View v)
    {
        Intent in=new Intent(this,Linkedin.class);
        startActivity(in);
    }
    public void twitter(View v)
    {
        Intent in=new Intent(this,Twit.class);
        startActivity(in);
    }

}
