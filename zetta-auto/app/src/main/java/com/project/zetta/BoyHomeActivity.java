package com.project.zetta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.zetta.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BoyHomeActivity extends AppCompatActivity {

    private Button logoutbtn;
    private androidx.cardview.widget.CardView manage_profile, manage_delivery, manage_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boy_home);
        logoutbtn = (Button)findViewById(R.id.logoutbtn);
        manage_delivery = (androidx.cardview.widget.CardView)findViewById(R.id.manage_delivery);
        manage_profile = (androidx.cardview.widget.CardView)findViewById(R.id.manage_profile);
        manage_order = (androidx.cardview.widget.CardView)findViewById(R.id.manage_order);


        // update the status
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("boys");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("status", "Online");
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
        // update the status

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update the status
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("boys");
                HashMap<String, Object> userMap = new HashMap<>();
                userMap. put("status", "Offline");
                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                // update the status
                Intent intent = new Intent(BoyHomeActivity.this, logout.class);
                startActivity(intent);
                finish();
            }
        });

        manage_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoyHomeActivity.this, BoyProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });


        manage_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoyHomeActivity.this, BoyManageOrder.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(BoyHomeActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Exit");

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

                                // When the user click yes button
                                // then app will close
                                // update the status
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("boys");
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("status", "Offline");
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                                // update the status
                                finish();
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
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}