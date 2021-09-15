package com.project.zetta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    private String phone, whoami;
    private EditText register_password_input, register_password_input_confirm;
    private Button confirm_btn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        phone = getIntent().getStringExtra("phone");
        whoami = getIntent().getStringExtra("whoami");
        register_password_input = (EditText)findViewById(R.id.register_password_input);
        register_password_input_confirm = (EditText)findViewById(R.id.register_password_input_confirm);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDetails();
            }
        });
    }

    private void checkDetails() {
        if(register_password_input.getText().toString().equals(""))
        {
            Toast.makeText(ChangePasswordActivity.this, "provide password" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(register_password_input_confirm.getText().toString().equals(""))
        {
            Toast.makeText(ChangePasswordActivity.this, "Confirm password" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(register_password_input.getText().toString().length() <= 8 )
        {
            Toast.makeText(ChangePasswordActivity.this, "Please follow password requirement" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(register_password_input.getText().toString().equals(register_password_input_confirm.getText().toString()))
        {
            changepass();

            loadingBar = new ProgressDialog(this);
            // make a delay.
            loadingBar.setTitle("Change password");
            loadingBar.setMessage("We are changing your password. Plesase wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingBar.dismiss();
                    Toast.makeText(getApplication(), "Password changed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);

        }
        else
        {
            Toast.makeText(getApplication(), "Password don't match", Toast.LENGTH_SHORT).show();
            return;
        }


    }
    private DatabaseReference mDatabase;
    private void changepass() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(whoami).child(phone).child("password").setValue(register_password_input.getText().toString());
    }
}