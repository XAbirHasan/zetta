package com.project.zetta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.project.zetta.Model.Product;
import com.project.zetta.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class GiveRatingActivity extends AppCompatActivity {



    private TextView productName, productDescription, productPrice, product_origin, product_warrenty;
    private ImageView productImage;
    private String productID = "";
    private String imgUrl = "";
    private RatingBar ratingBar;
    private Button give_ratingBtn;
    private DatabaseReference ProductsRef;

    private EditText comment;


    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rating);
        productID = getIntent().getStringExtra("pid");
        loadingBar = new ProgressDialog(this);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Ratings");

        productName = (TextView)findViewById(R.id.product_name);
        productDescription = (TextView)findViewById(R.id.product_description);
        productPrice = (TextView)findViewById(R.id.product_price);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        product_origin = (TextView)findViewById(R.id.product_origin);
        product_warrenty = (TextView)findViewById(R.id.product_warrenty);

        productImage = (ImageView) findViewById(R.id.product_image);

        give_ratingBtn = (Button) findViewById(R.id.give_ratingBtn);
        comment = (EditText)findViewById(R.id.comment);

        give_ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// give a warning then yes delete....
                // Create the object of
                // AlertDialog Builder class
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(GiveRatingActivity.this);

                // Set the message show for the Alert time
                builder.setMessage("Rating this product ?");

                // Set Alert Title
                builder.setTitle("Rating !");

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
                                        // then dialog box is canceled.
                                        /// update the ratting or add to database;
                                        if(comment.getText().toString().isEmpty())
                                        {
                                            Toast.makeText(getApplicationContext(), "Write your comments", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            updateRating();
                                        }



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
                                    }
                                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();

            }
        });

        getProductDetails(productID);
    }

    private void updateRating() {

        loadingBar.setTitle("Add Rating");
        loadingBar.setMessage("Please wait while we are adding the product rating.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("rating", ratingBar.getRating());
        productMap.put("comment", comment.getText().toString());

        ProductsRef.child(productID).child(Prevalent.currentOnlineUser.getPhone()).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();
                            loadingBar.dismiss();
                            Toast.makeText(GiveRatingActivity.this, "Ratting added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(GiveRatingActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

                    productName.setText(product.getPname());
                    productPrice.setText(product.getPrice());
                    productDescription.setText("Description : " + product.getDescription());
                    Picasso.get().load(product.getImage()).into(productImage);
                    imgUrl = product.getImage();
                    product_warrenty.setText("Warrenty : "+product.getWarrenty());
                    product_origin.setText("Origin : " + product.getOrigin());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}