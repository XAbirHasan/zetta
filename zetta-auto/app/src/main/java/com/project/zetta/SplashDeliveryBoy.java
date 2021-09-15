package com.project.zetta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashDeliveryBoy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_delivery_boy);

        final String permission = getIntent().getStringExtra("permission");



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(permission.equals("none"))
                {
                    Intent intent = new Intent(SplashDeliveryBoy.this, BoyLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashDeliveryBoy.this, BoyHomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }
}