package com.project.zetta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.project.zetta.Model.Product;
import com.project.zetta.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class EditiProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private String Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private TextView close, whichImage;
    private EditText product_discount, product_quantity, product_origin, product_warrenty;

    private Spinner spinnerproduct_origin, spinnerproduct_warrenty;

    private String pid = "";



    private ImageView select_product_image2, select_product_image3, select_product_image4, select_product_image5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editi_product);
        pid = getIntent().getStringExtra("pid");

        Toast.makeText(getApplicationContext(), pid, Toast.LENGTH_SHORT).show();


        getProductDetails(pid);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        product_discount = (EditText) findViewById(R.id.product_discount);
        product_quantity = (EditText) findViewById(R.id.product_quantity);
        product_origin = (EditText) findViewById(R.id.product_origin);
        product_warrenty = (EditText) findViewById(R.id.product_warrenty);

        whichImage = (TextView)findViewById(R.id.whichImage);

        close = (TextView)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                whichImage.setText("1");
                OpenGallery();
            }
        });

        // other images
        select_product_image2 = (ImageView)findViewById(R.id.select_product_image2);
        select_product_image3 = (ImageView)findViewById(R.id.select_product_image3);
        select_product_image4 = (ImageView)findViewById(R.id.select_product_image4);
        select_product_image5 = (ImageView)findViewById(R.id.select_product_image5);

        select_product_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                whichImage.setText("2");
                OpenGallery();
            }
        });
        select_product_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                whichImage.setText("3");
                OpenGallery();
            }
        });
        select_product_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                whichImage.setText("4");
                OpenGallery();
            }
        });
        select_product_image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                whichImage.setText("5");
                OpenGallery();
            }
        });

        // end images

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });


        spinnerproduct_warrenty = (Spinner) findViewById(R.id.spinnerproduct_warrenty);
        spinnerproduct_origin = (Spinner)findViewById(R.id.spinnerproduct_origin);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.warrenty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerproduct_warrenty.setAdapter(adapter);
        spinnerproduct_warrenty.setOnItemSelectedListener(EditiProductActivity.this);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.origin, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerproduct_origin.setAdapter(adapter2);
        spinnerproduct_origin.setOnItemSelectedListener(EditiProductActivity.this);
    }

    private void getProductDetails(final String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Product product = snapshot.getValue(Product.class);

                    InputProductName.setText(product.getPname());
                    InputProductDescription.setText(product.getDescription());
                    InputProductPrice.setText(product.getPrice());
                    product_discount.setText(product.getDiscount());
                    product_quantity.setText(product.getQuantity());
                    product_origin.setText(product.getOrigin());
                    product_warrenty.setText(product.getWarrenty());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null && whichImage.getText().toString().equals("1"))
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null && whichImage.getText().toString().equals("2"))
        {
            Uri uri = data.getData();
            select_product_image2.setImageURI(uri);
        }

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null && whichImage.getText().toString().equals("3"))
        {
            Uri uri = data.getData();
            select_product_image3.setImageURI(uri);
        }

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null && whichImage.getText().toString().equals("4"))
        {
            Uri uri = data.getData();
            select_product_image4.setImageURI(uri);
        }
        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null && whichImage.getText().toString().equals("5"))
        {
            Uri uri = data.getData();
            select_product_image5.setImageURI(uri);
        }

    }


    private void ValidateProductData()
    {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();


        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }

        else if (product_discount.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please write product discount...", Toast.LENGTH_SHORT).show();
        }
        else if (product_quantity.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please write product quantity...", Toast.LENGTH_SHORT).show();
        }
        else if (product_origin.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please write product origin...", Toast.LENGTH_SHORT).show();
        }
        else if (product_warrenty.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please write product warrenty...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        Random r = new Random();

        int rand1 = r.nextInt(1000)*1;
        int rand2 = r.nextInt(1000)*2;
        int rand3 = r.nextInt(1000)*3;
        int rand4 = r.nextInt(1000)*4;
        productRandomKey = Integer.toString(rand1)
                + Integer.toString(rand2)
                + Integer.toString(rand3)
                + Integer.toString(rand4)
                + saveCurrentDate + saveCurrentTime;



        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + pid + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(EditiProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(EditiProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(EditiProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", pid);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("status", "Not Shipped");
        productMap.put("seller", Prevalent.currentOnlineUser.getPhone());

        // product_discount, product_quantity, product_origin, product_warrenty;
        productMap.put("discount", product_discount.getText().toString());
        productMap.put("quantity", product_quantity.getText().toString());
        productMap.put("origin", product_origin.getText().toString());
        productMap.put("warrenty", product_warrenty.getText().toString());
        productMap.put("sold", String.valueOf(0));

        ProductsRef.child(pid).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();
                            loadingBar.dismiss();
                            Toast.makeText(EditiProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(EditiProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;

        if(spin.getId() == R.id.spinnerproduct_warrenty)
        {
            String text = parent.getItemAtPosition(position).toString();
            product_warrenty.setText(text);
        }

        if(spin.getId() == R.id.spinnerproduct_origin)
        {
            String text = parent.getItemAtPosition(position).toString();
            product_origin.setText(text);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}