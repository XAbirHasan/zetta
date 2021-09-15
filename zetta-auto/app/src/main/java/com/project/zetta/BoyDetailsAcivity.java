package com.project.zetta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BoyDetailsAcivity extends AppCompatActivity {

    de.hdodenhof.circleimageview.CircleImageView profileImageView;
    private TextView fullNameEditText, userPhoneEditText, addressEditText, settings_date, settings_gender;
    private String deliveryBoy;
    private Button hire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boy_details_acivity);
        deliveryBoy = getIntent().getStringExtra("phone");

        profileImageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (TextView) findViewById(R.id.settings_full_name);
        userPhoneEditText = (TextView) findViewById(R.id.settings_phone_number);
        addressEditText = (TextView) findViewById(R.id.settings_address);

        settings_date = (TextView) findViewById(R.id.settings_date);
        settings_gender = (TextView) findViewById(R.id.settings_gender);

        hire = (Button)findViewById(R.id.hire);

        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText, settings_date, settings_gender, deliveryBoy);

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update to hire section.
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +  deliveryBoy));
                startActivity(callIntent);
            }
        });


    }

    private void userInfoDisplay(final de.hdodenhof.circleimageview.CircleImageView profileImageView, final TextView fullNameEditText, final TextView userPhoneEditText, final TextView addressEditText, final TextView settings_date, final TextView settings_gender, final String deliveryBoy)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("boys").child(deliveryBoy);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String dateofBirth = dataSnapshot.child("dob").getValue().toString();
                        String gender = dataSnapshot.child("gender").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                        settings_date.setText(dateofBirth);
                        settings_gender.setText(gender);

                    }
                    else
                    {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();

                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}