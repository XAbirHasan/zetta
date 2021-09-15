package com.project.zetta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.zetta.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText InputPhoneNumber;
    private com.rilixtech.widget.countrycodepicker.CountryCodePicker ccp;
    private Button otp;
    private ProgressDialog loadingBar;
    private String whoami;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        whoami = getIntent().getStringExtra("whoami");

        InputPhoneNumber = (EditText) findViewById(R.id.phone_number_edt);
        ccp = (com.rilixtech.widget.countrycodepicker.CountryCodePicker)findViewById(R.id.ccp);
        otp = (Button)findViewById(R.id.otpBtn);
        loadingBar = new ProgressDialog(this);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDetalis();
            }
        });
    }
    String verificationId;
    private void checkDetalis() {


        String phone = InputPhoneNumber.getText().toString();
        String phoneCpc = ccp.getSelectedCountryCodeWithPlus();


        phone = phoneCpc + phone;


        if (InputPhoneNumber.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Account Varification");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            allowAccessToAccount(phone);
        }
    }

    private void allowAccessToAccount(final String phone) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(whoami).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(whoami).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        Random r = new Random();
                        String a, b, c, d, e, f;
                        a = Integer.toString(r.nextInt(10));
                        b = Integer.toString(r.nextInt(10));
                        c = Integer.toString(r.nextInt(10));
                        d = Integer.toString(r.nextInt(10));
                        e = Integer.toString(r.nextInt(10));
                        f = Integer.toString(r.nextInt(10));

                        verificationId = a + b + c + d + e + f;
                       // sendotp(phone);
                        enableUserManuallyInputCode(phone);

                    }
                }
                else
                {
                    Toast.makeText(ForgetPasswordActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void sendotp(final String phone) {
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone,
//                60,
//                TimeUnit.SECONDS,
//                ForgetPasswordActivity.this,
//                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//
//
//                        Toast.makeText(ForgetPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//
//
//                        Intent intent = new Intent(getApplicationContext(), GetOTPActivity.class);
//                        intent.putExtra("mobile",phone);
//                        intent.putExtra("verificationId",verificationId);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//        );
//    }


    private void enableUserManuallyInputCode(String phone) {
        Toast.makeText(ForgetPasswordActivity.this, "OTP is sending please wait...", Toast.LENGTH_SHORT).show();
        loadingBar.dismiss();
        Intent intent = new Intent(ForgetPasswordActivity.this, GetOTPActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("verificationId", verificationId);
        intent.putExtra("whoami", whoami);
        startActivity(intent);
        finish();
    }
}