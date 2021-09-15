package com.project.zetta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Model.Order;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class VendorManageDeliveryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView allOrder, delivered, noItem, whichType;
    private View allOrderView, deliveredView;

    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_manage_delivery);
        allOrder = (TextView)findViewById(R.id.allOrder);
        delivered = (TextView)findViewById(R.id.delivered);

        allOrderView = (View)findViewById(R.id.allOrderView);
        deliveredView = (View)findViewById(R.id.deliveredView);

        noItem = (TextView)findViewById(R.id.noItem);
        whichType = (TextView)findViewById(R.id.whichType);

        loadingBar = new ProgressDialog(this);

        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allOrderView.setVisibility(View.VISIBLE);
                allOrder.setTextColor(Color.WHITE);

                delivered.setTextColor(Color.BLACK);
                deliveredView.clearAnimation();
                deliveredView.setVisibility(View.GONE);
                whichType.setText("Confirm");
                noItem.setText("Order not found");
                onStart();


            }
        });
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delivered.setTextColor(Color.WHITE);
                deliveredView.setVisibility(View.VISIBLE);

                allOrder.setTextColor(Color.BLACK);
                allOrderView.clearAnimation();
                allOrderView.setVisibility(View.GONE);
                whichType.setText("Delivered");
                noItem.setText("Order not found");
                onStart();

            }
        });

        // never forget to add this it took 2hours to find what is the problem.
        recyclerView = findViewById(R.id.orderlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(whichType.getText().toString().equals("Confirm"))
        {
            findConfirm();
        }
        else if(whichType.getText().toString().equals("Delivered"))
        {
            findDelivered();
        }
    }

    private void findDelivered() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        FirebaseRecyclerOptions<Order> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(databaseReference.orderByChild("seller").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone()), Order.class)
                        .build();
        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull final Order order) {
                        orderViewHolder.product_name_order.setText(order.getPname());
                        orderViewHolder.product_quantity_order.setText("Quantity : " + order.getQuantity());
                        orderViewHolder.product_price_per_order.setText("Price : RM " + order.getPrice());
                        float quantity = Float.parseFloat(order.getQuantity());
                        float perPrice = Float.parseFloat(order.getPrice());
                        float totalPrice = quantity * perPrice;
                        orderViewHolder.product_price_total_order.setText("Total : RM " + String.format("%.2f", totalPrice));
                        Picasso.get().load(order.getImage()).into(orderViewHolder.product_image_order);
                        orderViewHolder.status_order.setText("Status : "+ order.getStatus());

                        orderViewHolder.status_order.setTextColor(Color.rgb(76, 175, 80));
                        noItem.setText("");
                        if(order.getStatus().equals("Delivered"))
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.GONE);
                        }

                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
                        return orderViewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void findConfirm() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        FirebaseRecyclerOptions<Order> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(databaseReference.orderByChild("seller").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone()), Order.class)
                        .build();

        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull final Order order) {
                        orderViewHolder.product_name_order.setText(order.getPname());
                        orderViewHolder.product_quantity_order.setText("Quantity : " + order.getQuantity());
                        orderViewHolder.product_price_per_order.setText("Price : RM " + order.getPrice());
                        float quantity = Float.parseFloat(order.getQuantity());
                        float perPrice = Float.parseFloat(order.getPrice());
                        float totalPrice = quantity * perPrice;
                        orderViewHolder.product_price_total_order.setText("Total : RM " + String.format("%.2f", totalPrice));
                        Picasso.get().load(order.getImage()).into(orderViewHolder.product_image_order);
                        orderViewHolder.status_order.setText("Status : "+ order.getStatus());

                        orderViewHolder.status_order.setTextColor(Color.rgb(255, 193, 7));


                        if(order.getStatus().equals("Confirm"))
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.GONE);
                        }
                        // go for placed order..
                        orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence choose[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(VendorManageDeliveryActivity.this);
                                builder.setTitle("Confirm Delivery");
                                builder.setItems(choose, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0)
                                        {
                                            loadingBar.setTitle("Add to Delivered");
                                            loadingBar.setMessage("Please wait while we are confirming it.");
                                            loadingBar.setCanceledOnTouchOutside(false);
                                            loadingBar.show();

                                            // make confrim and upate in data base.
                                            // make confrim and upate in data base.
                                            DatabaseReference confrimDabase = FirebaseDatabase.getInstance().getReference().child("Orders");
                                            HashMap<String, Object> updateMap = new HashMap<>();
                                            updateMap.put("status","Delivered");

                                            confrimDabase.child(order.getKey()).updateChildren(updateMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                loadingBar.dismiss();
                                                                Toast.makeText(VendorManageDeliveryActivity.this, "Order Sent for Delivery..", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else
                                                            {
                                                                loadingBar.dismiss();
                                                                String message = task.getException().toString();
                                                                Toast.makeText(VendorManageDeliveryActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                        }
                                        if(which == 1)
                                        {
                                            Toast.makeText(getApplication(), "Order not Confirm yet!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });


                        noItem.setText("");
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
                        return orderViewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    /*

    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        final String phoneKey = ds.getKey();
                        getNextkey(phoneKey);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNextkey(final String phoneKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(phoneKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        final String orderKey = ds.getKey();

                        /// this go to which order goes palced...not the all.
                        findOrder(phoneKey,orderKey);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findOrder(final String phoneKey, final String orderKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(phoneKey).child(orderKey).child("Product");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        final String productKey = ds.getKey();
                        getSeller(phoneKey,orderKey,productKey);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getSeller(final String phoneKey, final String orderKey,final String productKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(phoneKey).child(orderKey).child("Product");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(productKey).exists())
                {
                    Order order = snapshot.child(productKey).getValue(Order.class);
//                    Toast.makeText(VendorManageOrderActivity.this, phoneKey + "\n"
//                            + orderKey + "\n" + productKey
//                            + "\n" +order.getSeller(), Toast.LENGTH_SHORT).show();
                    if(order.getSeller().equals(Prevalent.currentOnlineUser.getPhone()))
                    {
                        if(whichType.getText().toString().equals("Confirm"))
                        {
                            findPlacedOrder(phoneKey,orderKey,productKey);
                        }
                        if(whichType.getText().toString().equals("Delivered"))
                        {
                            findConfirm(phoneKey,orderKey,productKey);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findConfirm(final String phoneKey,final String orderKey,final String productKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(phoneKey).child(orderKey);
        FirebaseRecyclerOptions<Order> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(databaseReference.child("Product").orderByChild("status").startAt("Delivered").endAt("Delivered"), Order.class)
                        .build();
        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull final Order order) {
                        orderViewHolder.product_name_order.setText(order.getPname());
                        orderViewHolder.product_quantity_order.setText("Quantity : " + order.getQuantity());
                        orderViewHolder.product_price_per_order.setText("Price : RM " + order.getPrice());
                        float quantity = Float.parseFloat(order.getQuantity());
                        float perPrice = Float.parseFloat(order.getPrice());
                        float totalPrice = quantity * perPrice;
                        orderViewHolder.product_price_total_order.setText("Total : RM " + String.format("%.2f", totalPrice));
                        Picasso.get().load(order.getImage()).into(orderViewHolder.product_image_order);
                        orderViewHolder.status_order.setText("Status : "+ order.getStatus());

                        orderViewHolder.status_order.setTextColor(Color.rgb(76, 175, 80));

                        noItem.setText("");
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
                        return orderViewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void findPlacedOrder(final String phoneKey,final String orderKey,final String productKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(phoneKey).child(orderKey);
        FirebaseRecyclerOptions<Order> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(databaseReference.child("Product").orderByChild("status").startAt("Confirm").endAt("Confirm"), Order.class)
                        .build();
        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull final Order order) {
                        orderViewHolder.product_name_order.setText(order.getPname());
                        orderViewHolder.product_quantity_order.setText("Quantity : " + order.getQuantity());
                        orderViewHolder.product_price_per_order.setText("Price : RM " + order.getPrice());
                        float quantity = Float.parseFloat(order.getQuantity());
                        float perPrice = Float.parseFloat(order.getPrice());
                        float totalPrice = quantity * perPrice;
                        orderViewHolder.product_price_total_order.setText("Total : RM " + String.format("%.2f", totalPrice));
                        Picasso.get().load(order.getImage()).into(orderViewHolder.product_image_order);
                        orderViewHolder.status_order.setText("Status : "+ order.getStatus());

                        orderViewHolder.status_order.setTextColor(Color.rgb(255, 193, 7));

                        // go for placed order..
                        orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence choose[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(VendorManageDeliveryActivity.this);
                                builder.setTitle("Confirm Delivery");
                                builder.setItems(choose, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0)
                                        {
                                            loadingBar.setTitle("Add to Delivered");
                                            loadingBar.setMessage("Please wait while we are confirming it.");
                                            loadingBar.setCanceledOnTouchOutside(false);
                                            loadingBar.show();

                                            // make confrim and upate in data base.
                                            DatabaseReference confrimDabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(phoneKey).child(orderKey);
                                            HashMap<String, Object> updateMap = new HashMap<>();
                                            updateMap.put("status","Delivered");

                                            confrimDabase.child("Product").child(order.getPid()).updateChildren(updateMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                loadingBar.dismiss();
                                                                Toast.makeText(VendorManageDeliveryActivity.this, "Order Sent for Delivery..", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else
                                                            {
                                                                loadingBar.dismiss();
                                                                String message = task.getException().toString();
                                                                Toast.makeText(VendorManageDeliveryActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                        }
                                        if(which == 1)
                                        {
                                            Toast.makeText(getApplication(), "Order not Confirm yet!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });


                        noItem.setText("");
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
                        return orderViewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

     */
}