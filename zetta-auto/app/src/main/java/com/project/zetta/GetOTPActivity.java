package com.project.zetta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GetOTPActivity extends AppCompatActivity {

    private TextView phoneNumber, reSendOTP, showOTPtimer;
    private EditText input_code1, input_code2, input_code3, input_code4, input_code5, input_code6;
    private Button varifyBtn;
    private String phone, verificationId, whoami;
    CountDownTimer countDownTimer;
    private boolean resendStart = true;
    private int timeleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_o_t_p);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        phoneNumber.setText(getIntent().getStringExtra("phone"));
        phone = getIntent().getStringExtra("phone");
        verificationId = getIntent().getStringExtra("verificationId");
        whoami = getIntent().getStringExtra("whoami");
        showOTPtimer = (TextView)findViewById(R.id.showOTPtimer);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendStart = false;
                timeleft = (int)millisUntilFinished/1000;
                showOTPtimer.setText("OPT will resend after "+Integer.toString(timeleft) + " Second.");
            }

            @Override
            public void onFinish() {
                Random r = new Random();
                String a, b, c, d, e, f;
                a = Integer.toString(r.nextInt(10));
                b = Integer.toString(r.nextInt(10));
                c = Integer.toString(r.nextInt(10));
                d = Integer.toString(r.nextInt(10));
                e = Integer.toString(r.nextInt(10));
                f = Integer.toString(r.nextInt(10));

                verificationId = a + b + c + d + e + f;
                showOTPtimer.setText("");
                sendnotification();
                resendStart = true;
            }
        };

        reSendOTP = (TextView)findViewById(R.id.reSendOTP);
        reSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendStart)countDownTimer.start();
            }
        });

        input_code1 = (EditText)findViewById(R.id.input_code1);
        input_code2 = (EditText)findViewById(R.id.input_code2);
        input_code3 = (EditText)findViewById(R.id.input_code3);
        input_code4 = (EditText)findViewById(R.id.input_code4);
        input_code5 = (EditText)findViewById(R.id.input_code5);
        input_code6 = (EditText)findViewById(R.id.input_code6);

        varifyBtn = (Button)findViewById(R.id.varifyBtn);
        sendnotification();
        setOTPinputs();
        varifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmpty();
            }
        });


    }


    private void sendnotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("OTP Message", "OTP Message", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(GetOTPActivity.this, "OTP Message");
        builder.setContentTitle("OTP Message");
        builder.setContentText("Your OTP is : '" + verificationId + "'. Don't share this with anyone.");
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setSmallIcon(R.drawable.ic_comment);
        builder.setCategory(Notification.CATEGORY_MESSAGE);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(GetOTPActivity.this);
        managerCompat.notify(1, builder.build());
    }

    private void checkEmpty() {
        if(input_code1.getText().toString().equals(""))
        {
            Toast.makeText(GetOTPActivity.this, "Fill OTP area" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(input_code2.getText().toString().equals(""))
        {
            Toast.makeText(GetOTPActivity.this, "Fill OTP area" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(input_code3.getText().toString().equals(""))
        {
            Toast.makeText(GetOTPActivity.this, "Fill OTP area" , Toast.LENGTH_SHORT).show();
            return;
        }


        if(input_code4.getText().toString().equals(""))
        {
            Toast.makeText(GetOTPActivity.this, "Fill OTP area" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(input_code5.getText().toString().equals(""))
        {
            Toast.makeText(GetOTPActivity.this, "Fill OTP area" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(input_code6.getText().toString().equals(""))
        {
            Toast.makeText(GetOTPActivity.this, "Fill OTP area" , Toast.LENGTH_SHORT).show();
            return;
        }
        String theCode;
        theCode =   input_code1.getText().toString()
                    +input_code2.getText().toString()
                    +input_code3.getText().toString()
                    +input_code4.getText().toString()
                    +input_code5.getText().toString()
                    +input_code6.getText().toString();
        if(verificationId.equals(theCode))
        {
            Intent intent = new Intent(GetOTPActivity.this, ChangePasswordActivity.class);
            intent.putExtra("phone", phone);
            intent.putExtra("whoami", whoami);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(GetOTPActivity.this, "OTP doesn't match" , Toast.LENGTH_SHORT).show();
        }
    }

    private void setOTPinputs()
    {
        input_code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    input_code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty())
                {
                    input_code1.requestFocus();
                }
                if(!s.toString().trim().isEmpty())
                {
                    input_code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty())
                {
                    input_code2.requestFocus();
                }
                if(!s.toString().trim().isEmpty())
                {
                    input_code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty())
                {
                    input_code3.requestFocus();
                }
                if(!s.toString().trim().isEmpty())
                {
                    input_code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty())
                {
                    input_code4.requestFocus();
                }
                if(!s.toString().trim().isEmpty())
                {
                    input_code6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().isEmpty())
                {
                    input_code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}