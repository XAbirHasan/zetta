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

import com.project.zetta.Model.Cart;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private TextView tPrice, noItem;
    private Button proceedBtn;
    private float calculateTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView =  (androidx.recyclerview.widget.RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tPrice = (TextView)findViewById(R.id.totalPrice);
        proceedBtn = (Button)findViewById(R.id.proceedBtn);
        noItem =(TextView)findViewById(R.id.noItem);

        tPrice.setText("Total Cost: RM " +String.format("%.2f", calculateTotal));

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(calculateTotal > 0) {
                    Intent intent = new Intent(CartActivity.this, ConfirmActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplication(), "Your Cart is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        noItem.setText("Cart is Empty");
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart list");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                .child("Product"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                noItem.setText("");
                cartViewHolder.productName.setText(cart.getPname());
                cartViewHolder.productQuantity.setText("Quantity : " + cart.getQuantity());
                cartViewHolder.productPrice.setText("Price : RM " + cart.getPrice());
                float quantity = Float.parseFloat(cart.getQuantity());
                float perPrice = Float.parseFloat(cart.getPrice());
                float totalPrice = quantity * perPrice;
                cartViewHolder.totalPrice.setText("Total : RM " + String.format("%.2f", totalPrice));
                calculateTotal += totalPrice;

                tPrice.setText("Total Cost: RM " +String.format("%.2f", calculateTotal));

                Picasso.get().load(cart.getImage()).into(cartViewHolder.productImage);

                // press eidit button
                cartViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(CartActivity.this, ProductDetails.class);
                        intent.putExtra("pid", cart.getPid());
                        intent.putExtra("key", cart.getKey());
                        startActivity(intent);
                        finish();
                    }
                });

                // press delete button
                cartViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Create the object of
                        // AlertDialog Builder class
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(CartActivity.this);

                        // Set the message show for the Alert time
                        builder.setMessage("Do you want to delete this product?");

                        // Set Alert Title
                        builder.setTitle("Delete Cart");

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
                                                        .child("Product").child(cart.getKey())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    calculateTotal -= (Float.parseFloat(cart.getQuantity()) * Float.parseFloat(cart.getPrice()));
                                                                    tPrice.setText("Total Cost: RM " +String.format("%.2f", calculateTotal));
                                                                    Toast.makeText(CartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
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
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);

                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}