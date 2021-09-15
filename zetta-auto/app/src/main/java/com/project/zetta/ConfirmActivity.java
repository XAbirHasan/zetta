package com.project.zetta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.zetta.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmActivity extends AppCompatActivity {

    private EditText name, phone, region, city, road, zip, address;
    private Button save;

    private String totalPrice = "";

    private com.rey.material.widget.CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        name = (EditText)findViewById(R.id.shipName);
        phone = (EditText)findViewById(R.id.shipNumber);
        region = (EditText)findViewById(R.id.shipRegion);
        city = (EditText)findViewById(R.id.shipCity);
        road = (EditText)findViewById(R.id.shipRoad);
        zip = (EditText)findViewById(R.id.shipZip);
        address = (EditText)findViewById(R.id.shipHomeAddress);

        save = (Button)findViewById(R.id.save_address_btn);
        checkBox = (com.rey.material.widget.CheckBox) findViewById(R.id.remember_me_chkb);

        getUserAddress(name, phone, region, city, road, zip, address);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty_info())
                {
                    // go for payment
                    Intent intent = new Intent(ConfirmActivity.this, PaymentActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("city", city.getText().toString());
                    intent.putExtra("road", road.getText().toString());
                    intent.putExtra("phone", phone.getText().toString());
                    intent.putExtra("house", address.getText().toString());
                    intent.putExtra("zip", zip.getText().toString());
                    intent.putExtra("region", region.getText().toString());
                    startActivity(intent);
                    finish();
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

    private void getUserAddress(final EditText name,final EditText phone,final EditText region,final EditText city,final EditText road,final EditText zip,final EditText address) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Address").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if(dataSnapshot.child("name").exists())
                    {
                        String getname, getphone, getregion, getcity, getroad, getzip, getaddress;

                        getname = dataSnapshot.child("name").getValue().toString();
                        getphone = dataSnapshot.child("phone").getValue().toString();
                        getregion = dataSnapshot.child("region").getValue().toString();
                        getcity = dataSnapshot.child("city").getValue().toString();
                        getroad = dataSnapshot.child("road").getValue().toString();
                        getzip = dataSnapshot.child("zip").getValue().toString();
                        getaddress = dataSnapshot.child("house").getValue().toString();

                        name.setText(getname);
                        phone.setText(getphone);
                        region.setText(getregion);
                        city.setText(getcity);
                        road.setText(getroad);
                        zip.setText(getzip);
                        address.setText(getaddress);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void show_alert()
    {
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(ConfirmActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to confirm your order ?");

        // Set Alert Title
        builder.setTitle("Confirm Order");

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

    private boolean isEmpty_info() {

        //cheking name, phone, region, city, road, zip, address;

        if(name.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide Name", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(phone.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide Phone Number", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(region.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide Region", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(city.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide City", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(road.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide Road", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(zip.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide ZIP", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(address.getText().toString().equals(""))
        {
            Toast.makeText(getApplication(), "Provide Home Address", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(!checkBox.isChecked())
        {
            Toast.makeText(getApplication(), "Confirm your Orders", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}