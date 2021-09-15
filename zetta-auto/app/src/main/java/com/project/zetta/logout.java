package com.project.zetta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;

import io.paperdb.Paper;

public class logout extends AppCompatActivity {

    private Timer timer;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        loadingBar = new ProgressDialog(this);
        // make a delay.
        loadingBar.setTitle("Logout");
        loadingBar.setMessage("logging out");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingBar.dismiss();
                Paper.book().destroy();
                finish();
            }
        }, 3000);

    }
}