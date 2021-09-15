package com.project.zetta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.zetta.Model.Wishlist;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.WishListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class WishlistActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private Button home;
    private TextView noItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        noItem = (TextView)findViewById(R.id.noItem);

        recyclerView =  (androidx.recyclerview.widget.RecyclerView)findViewById(R.id.cart_list);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        home = (Button)findViewById(R.id.nav_home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        noItem.setText("Wishlist is Empty");
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Wish list");
        FirebaseRecyclerOptions<Wishlist> options =
                new FirebaseRecyclerOptions.Builder<Wishlist>()
                        .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                .child("Product"), Wishlist.class).build();

        FirebaseRecyclerAdapter<Wishlist, WishListViewHolder> adapter
                = new FirebaseRecyclerAdapter<Wishlist, WishListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishListViewHolder wishListViewHolder, int i, @NonNull final Wishlist wishlist) {

                noItem.setText("");
                wishListViewHolder.productName.setText(wishlist.getPname());
                wishListViewHolder.productQuantity.setText("Quantity : " + wishlist.getQuantity());
                wishListViewHolder.productPrice.setText("Price : RM " + wishlist.getPrice());
                float quantity = Float.parseFloat(wishlist.getQuantity());
                float perPrice = Float.parseFloat(wishlist.getPrice());
                float totalPrice = quantity * perPrice;
                wishListViewHolder.totalPrice.setText("Total : RM " + String.format("%.2f", totalPrice));
                Picasso.get().load(wishlist.getImage()).into(wishListViewHolder.productImage);

                wishListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishlistActivity.this, ProductDetails.class);
                        intent.putExtra("pid", wishlist.getPid());
                        startActivity(intent);
                    }
                });


                wishListViewHolder.addtoCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCart(wishlist.getPid(), wishlist.getPname(), wishlist.getImage(), wishlist.getPrice(), wishlist.getQuantity(), wishlist.getSeller());
                    }
                });

                // press delete button
                wishListViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Create the object of
                        // AlertDialog Builder class
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(WishlistActivity.this);

                        // Set the message show for the Alert time
                        builder.setMessage("Do you want to delete this product?");

                        // Set Alert Title
                        builder.setTitle("Delete Wishlist");

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
                                                cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                                        .child("Product").child(wishlist.getKey())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(WishlistActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                                    onStart();
                                                                }
                                                            }
                                                        });
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
            }

            @NonNull
            @Override
            public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
                WishListViewHolder holder = new WishListViewHolder(view);

                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void addToCart(String productID, String pname, String image, String price, String quantity, String seller) {

        String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart list");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", pname);
        cartMap.put("price", price);
        cartMap.put("image", image);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", quantity);
        cartMap.put("status", "Not Shipped");
        cartMap.put("seller", seller);
        cartMap.put("user", Prevalent.currentOnlineUser.getPhone());

        // for randomkey
        Random r = new Random();
        int rand1 = r.nextInt(1000)*1;
        int rand2 = r.nextInt(1000)*2;
        int rand3 = r.nextInt(1000)*3;
        int rand4 = r.nextInt(1000)*4;
        String productRandomKey = Integer.toString(rand1)
                + Integer.toString(rand2)
                + Integer.toString(rand3)
                + Integer.toString(rand4)
                + saveCurrentDate + saveCurrentTime;
        cartMap.put("key", productRandomKey);

        cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Product").child(productRandomKey).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(WishlistActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ProductDetails.this, CustomerHomeActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }


}