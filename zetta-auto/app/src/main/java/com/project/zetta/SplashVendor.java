package com.project.zetta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashVendor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_vendor);
        final String permission = getIntent().getStringExtra("permission");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(permission.equals("none"))
                {
                    Intent intent = new Intent(SplashVendor.this, VendorLogin_page.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashVendor.this, VendorHomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }
}