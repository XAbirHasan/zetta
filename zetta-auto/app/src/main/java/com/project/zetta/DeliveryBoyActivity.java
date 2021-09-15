package com.project.zetta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Model.NewBoy;
import com.project.zetta.Model.Users;
import com.project.zetta.ViewHolder.BoyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DeliveryBoyActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView allOrder, delivered, noItem, whichType;
    private View allOrderView, deliveredView;


    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy);

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
                whichType.setText("Online");
                noItem.setText("Delivery boy not found");
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
                whichType.setText("Offline");
                noItem.setText("Delivery boy not found");
                onStart();

            }
        });

        // never forget to add this it took 2hours to find what is the problem.
        recyclerView = findViewById(R.id.boylist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // firebase

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(whichType.getText().toString().equals("Online"))
        {
            findOnline();
        }
        else if(whichType.getText().toString().equals("Offline"))
        {
            findOffline();
        }
    }

    private void findOffline() {
        noItem.setText("Delivery boy not found");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("boys");
        FirebaseRecyclerOptions<NewBoy> options =
                new FirebaseRecyclerOptions.Builder<NewBoy>()
                        .setQuery(databaseReference.orderByChild("status").startAt("Offline").endAt("Offline"), NewBoy.class).build();

        FirebaseRecyclerAdapter<NewBoy, BoyViewHolder> adapter
                = new FirebaseRecyclerAdapter<NewBoy, BoyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoyViewHolder boyViewHolder, int i, @NonNull final NewBoy users) {
                noItem.setText("");
                boyViewHolder.boy_name.setText(users.getName());
                boyViewHolder.boy_phone.setText(users.getPhone());
            }

            @NonNull
            @Override
            public BoyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boy_item_smaller, parent, false);
                BoyViewHolder holder = new BoyViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void findOnline() {

        noItem.setText("Delivery boy not found");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("boys");
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(databaseReference.orderByChild("status").startAt("Online").endAt("Online"), Users.class).build();

        FirebaseRecyclerAdapter<Users, BoyViewHolder> adapter
                = new FirebaseRecyclerAdapter<Users, BoyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoyViewHolder boyViewHolder, int i, @NonNull final Users users) {
                noItem.setText("");
                boyViewHolder.boy_name.setText(users.getName());
                boyViewHolder.boy_phone.setText(users.getPhone());
                if(!users.getImage().equals(null))
                {
                    Picasso.get().load(users.getImage()).into(boyViewHolder.boyProfile);
                }
                boyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DeliveryBoyActivity.this, BoyDetailsAcivity.class);
                        intent.putExtra("phone", users.getPhone());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public BoyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boy_item_smaller, parent, false);
                BoyViewHolder holder = new BoyViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    /*

    private void findDelivered() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("boys");
        FirebaseRecyclerOptions<Boy> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Boy>()
                        .setQuery(databaseReference.orderByChild("seller").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone()), Boy.class)
                        .build();
        FirebaseRecyclerAdapter<Boy, BoyViewHolder> adapter =
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

     */
}