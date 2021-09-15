package com.project.zetta;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.project.zetta.Prevalent.DatePickerFragment;
import com.project.zetta.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class VendorProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , AdapterView.OnItemSelectedListener{

    de.hdodenhof.circleimageview.CircleImageView profileImageView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText, settings_date, settings_company_number;
    private EditText settings_gender;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    private Spinner spinnerGender;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    private Button button, changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Vendors pictures");

        profileImageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEditText = (EditText) findViewById(R.id.settings_phone_number);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        profileChangeTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);

        settings_company_number = (EditText) findViewById(R.id.settings_company_number);


        settings_date = (EditText) findViewById(R.id.settings_date);
        settings_gender = (EditText) findViewById(R.id.settings_gender);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);


        changePassword = (Button)findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                intent.putExtra("whoami", "Vendors");
                startActivity(intent);
                finish();
            }
        });


        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText, settings_date, settings_gender, spinnerGender, settings_company_number);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(VendorProfileActivity.this);


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(VendorProfileActivity.this, VendorHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    if(userInfoSaved()){
                        updateOnlyUserInfo();
                    }

                }
            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(VendorProfileActivity.this);
            }
        });

        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String currentDateString = currentDate.format(c.getTime());
        settings_date.setText(currentDateString);
    }


    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Vendors");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phoneOrder", userPhoneEditText.getText().toString());
        userMap.put("dob", settings_date.getText().toString());
        userMap.put("gender", settings_gender.getText().toString());
        userMap.put("company", settings_company_number.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(VendorProfileActivity.this, logout.class));
        Toast.makeText(VendorProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(VendorProfileActivity.this, VendorProfileActivity.class));
            finish();
        }
    }




    private boolean userInfoSaved()
    {
        if (settings_company_number.getText().toString().equals(""))
        {
            Toast.makeText(this, "Company number is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fullNameEditText.getText().toString().equals(""))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (addressEditText.getText().toString().equals(""))
        {
            Toast.makeText(this, "Address is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (userPhoneEditText.getText().toString().equals(""))
        {
            Toast.makeText(this, "Phone number is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(settings_date.getText().toString().equals(""))
        {
            Toast.makeText(this, "DOB is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (settings_gender.getText().toString().equals(""))
        {
            Toast.makeText(this, "Gender is mandatory.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(checker.equals("clicked"))
        {
            if(uploadImage())
            {
                return true;
            }
            else return false;
        }

        return true;
    }



    private boolean uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Vendors");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", fullNameEditText.getText().toString());
                                userMap. put("address", addressEditText.getText().toString());
                                userMap. put("phoneOrder", userPhoneEditText.getText().toString());
                                userMap. put("image", myUrl);
                                userMap.put("dob", settings_date.getText().toString());
                                userMap.put("gender", settings_gender.getText().toString());
                                userMap.put("company", settings_company_number.getText().toString());
                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(VendorProfileActivity.this, logout.class));
                                Toast.makeText(VendorProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(VendorProfileActivity.this, "Error.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText, final EditText settings_date, final EditText settings_gender, final Spinner spinnerGender,final EditText settings_company_number)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Vendors").child(Prevalent.currentOnlineUser.getPhone());

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
                        String company = dataSnapshot.child("company").getValue().toString();


                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                        settings_company_number.setText(company);

                        settings_date.setText(dateofBirth);
                        settings_gender.setText(gender);

                        if(gender.equals("male"))
                        {
                            spinnerGender.setSelection(0);
                        }
                        else if(gender.equals("femele"))
                        {
                            spinnerGender.setSelection(1);
                        }
                        else
                        {
                            spinnerGender.setSelection(2);
                        }
                    }
                    else
                    {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String company = dataSnapshot.child("company").getValue().toString();

                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        settings_company_number.setText(company);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        settings_gender.setText(text);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}