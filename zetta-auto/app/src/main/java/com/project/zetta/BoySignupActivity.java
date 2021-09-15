package com.project.zetta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class BoySignupActivity extends AppCompatActivity {


    private TextView loginLink;

    private EditText username, mobile, password, re_password;
    private Button regBtn;
    private com.rey.material.widget.CheckBox checkBox;

    private ProgressDialog loadingBar;

    private com.rilixtech.widget.countrycodepicker.CountryCodePicker ccp;


    // this is for password check
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boy_signup);
        loginLink = (TextView)findViewById(R.id.login_panel_link);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoySignupActivity.this, BoyLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        username = (EditText) findViewById(R.id.register_username_input);
        mobile = (EditText) findViewById(R.id.register_phone_number_input);
        password = (EditText) findViewById(R.id.register_password_input);
        re_password = (EditText) findViewById(R.id.register_password_input_confirm);
        regBtn = (Button) findViewById(R.id.register_btn);
        checkBox = (com.rey.material.widget.CheckBox) findViewById(R.id.remember_me_chkb);

        loadingBar = new ProgressDialog(this);

        ccp = (com.rilixtech.widget.countrycodepicker.CountryCodePicker)findViewById(R.id.ccp);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(singUP())
                {

                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_alert();
            }
        });
    }
    private boolean singUP()
    {
        final String nam, phn, pass, repass;
        String phone, mobleCcp;
        nam =  username.getText().toString();
        phone = mobile.getText().toString();
        mobleCcp = ccp.getSelectedCountryCodeWithPlus();
        phn = mobleCcp + phone;
        pass = password.getText().toString();
        repass = re_password.getText().toString();

        if(check_empty())
        {
            return  false;
        }
        if(!pass.equals(repass))
        {
            Toast.makeText(getApplication(), "Password don't match", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
            return false;
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("boys").child(phn).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phn);
                    userdataMap.put("password", pass);
                    userdataMap.put("name", nam);

                    RootRef.child("boys").child(phn).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(BoySignupActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(BoySignupActivity.this, BoyLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(BoySignupActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(BoySignupActivity.this, "This " + phn + " already exists.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(BoySignupActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return  false;
    }
    private void show_alert()
    {
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(BoySignupActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Accept all our Terms and Conditions");

        // Set Alert Title
        builder.setTitle("Terms and Conditions");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                checkBox.setChecked(true);
                                dialog.cancel();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                checkBox.setChecked(false);
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
    private boolean check_empty()
    {
        if(username.getText().toString().isEmpty())
        {
            Toast.makeText(getApplication(), "Provide username", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(mobile.getText().toString().isEmpty())
        {
            Toast.makeText(getApplication(), "Provide phone number", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(password.getText().toString().isEmpty())
        {
            Toast.makeText(getApplication(), "Provide password", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()) {
            password.setError("Password too weak");
            return true;
        }
        if(re_password.getText().toString().isEmpty())
        {
            Toast.makeText(getApplication(), "Confirm password", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(!checkBox.isChecked())
        {
            Toast.makeText(getApplication(), "Terms and Conditions", Toast.LENGTH_SHORT).show();
            return true;
        }

        loadingBar.setTitle("Creating Account");
        loadingBar.setMessage("Please wait, while we are checking the credentials.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        return false;
    }
}