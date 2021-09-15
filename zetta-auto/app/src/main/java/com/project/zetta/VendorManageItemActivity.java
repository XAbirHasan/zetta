package com.project.zetta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Model.Product;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class VendorManageItemActivity extends AppCompatActivity {

    private TextView close, additem;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_manage_item);

        close = (TextView) findViewById(R.id.close);
        additem = (TextView) findViewById(R.id.additem);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VendorManageItemActivity.this, VendroAddProductActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.product_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        sumVal = (TextView) findViewById(R.id.sum);
        countVal = (TextView) findViewById(R.id.count);

    }

    private DatabaseReference ProductsRef;
    private TextView sumVal, countVal;

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(ProductsRef.orderByChild("seller").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone()), Product.class)
                        .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull final Product product) {

                        // for safe.
                        sumVal.setText("0");
                        countVal.setText("1");
                        productViewHolder.txtProductName.setText(product.getPname());
                        productViewHolder.txtProductPrice.setText("Price : RM " + product.getPrice());
                        Picasso.get().load(product.getImage()).into(productViewHolder.imageView);

                        // go for delete product.
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence choose[] = new CharSequence[]
                                        {
                                                "Delete",
                                                "Edit"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(VendorManageItemActivity.this);
                                builder.setTitle("Options :");
                                builder.setItems(choose, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0)
                                        {
                                            /// give a warning then yes delete....
                                            // Create the object of
                                            // AlertDialog Builder class
                                            AlertDialog.Builder builder
                                                    = new AlertDialog
                                                    .Builder(VendorManageItemActivity.this);

                                            // Set the message show for the Alert time
                                            builder.setMessage("Do you want to delete this product?");

                                            // Set Alert Title
                                            builder.setTitle("Delete Product !");

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
                                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
                                                                    databaseReference.child(product.getPid())
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {

                                                                                        // remove the rating as well for that product
                                                                                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Ratings");
                                                                                        databaseReference2.child(product.getPid())
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {

                                                                                                            Toast.makeText(VendorManageItemActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                                                                            onStart();
                                                                                                        }
                                                                                                    }
                                                                                                });
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
                                        if(which == 1)
                                        {
                                            Intent intent = new Intent(getApplicationContext(), EditiProductActivity.class);
                                            intent.putExtra("pid", product.getPid());
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(product.getPid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    int sum = 0;
                                    int c = 0;
                                    for (DataSnapshot ds : snapshot.getChildren())
                                    {
                                        Map<String, Object>map =  (Map<String, Object>) ds.getValue();
                                        Object ratings = map.get("rating");
                                        int val = Integer.parseInt(String.valueOf(ratings));
                                        c += 1;
                                        sum += val;
                                        float avgrating = (float) sum / c;
                                        String rating = String.format("%.2f", avgrating);
                                        productViewHolder.ratingBar.setRating(Float.parseFloat(rating));
                                        productViewHolder.sumRating.setText("Ratings(" + String.valueOf(c) + ")");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Picasso.get().load(product.getImage()).into(productViewHolder.imageView);
                        sumVal.setText("");
                        countVal.setText("");

                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}