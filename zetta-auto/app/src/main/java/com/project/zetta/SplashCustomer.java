package com.project.zetta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashCustomer extends AppCompatActivity {

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_customer);
        final String permission = getIntent().getStringExtra("permission");



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(permission.equals("none"))
                {
                    Intent intent = new Intent(SplashCustomer.this, CustomerLogin_page.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashCustomer.this, CustomerHomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }
}