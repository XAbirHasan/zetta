package com.project.zetta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.zetta.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

public class VendorHomeActivity extends AppCompatActivity {

    private Button logoutbtn;
    de.hdodenhof.circleimageview.CircleImageView userImg;

    androidx.cardview.widget.CardView manage_item, manage_order, manage_delivery, manage_payment, deliveryBoy, sales_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);
        manage_item = (androidx.cardview.widget.CardView)findViewById(R.id.manage_item);
        manage_order = (androidx.cardview.widget.CardView)findViewById(R.id.manage_order);
        manage_delivery = (androidx.cardview.widget.CardView)findViewById(R.id.manage_delivery);
        manage_payment = (androidx.cardview.widget.CardView)findViewById(R.id.manage_payment);
        deliveryBoy = (androidx.cardview.widget.CardView)findViewById(R.id.deliveryBoy);
        sales_report = (androidx.cardview.widget.CardView)findViewById(R.id.sales_report);


        manage_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorManageItemActivity.class);
                startActivity(intent);

            }
        });
        manage_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorManageOrderActivity.class);
                startActivity(intent);

            }
        });
        manage_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorManageDeliveryActivity.class);
                startActivity(intent);

            }
        });
        manage_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorManagePaymentActivity.class);
                startActivity(intent);

            }
        });
        deliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, DeliveryBoyActivity.class);
                startActivity(intent);
            }
        });
        sales_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorSalesReportActivity.class);
                startActivity(intent);

            }
        });


        logoutbtn = (Button)findViewById(R.id.logoutbtn);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, logout.class);
                startActivity(intent);
                finish();
            }
        });

        // set te profile picture
        userImg = findViewById(R.id.user_profile_image);
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(userImg);

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorHomeActivity.this, VendorProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(VendorHomeActivity.this);

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