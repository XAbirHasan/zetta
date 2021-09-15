package com.project.zetta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Model.Order;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView allOrder, delivered, noItem, whichType;
    private View allOrderView, deliveredView;
    private List<String> idArray;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        allOrder = (TextView)findViewById(R.id.allOrder);
        delivered = (TextView)findViewById(R.id.delivered);

        allOrderView = (View)findViewById(R.id.allOrderView);
        deliveredView = (View)findViewById(R.id.deliveredView);

        noItem = (TextView)findViewById(R.id.noItem);
        whichType = (TextView)findViewById(R.id.whichType);

        idArray = new ArrayList<String>();


        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allOrderView.setVisibility(View.VISIBLE);
                allOrder.setTextColor(Color.WHITE);

                delivered.setTextColor(Color.BLACK);
                deliveredView.clearAnimation();
                deliveredView.setVisibility(View.GONE);
                whichType.setText("All Orders");
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

    protected void onStart() {
        super.onStart();

            if(whichType.getText().toString().equals("All Orders"))
            {
                findAllOrder();
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
                        .setQuery(databaseReference.orderByChild("user").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone()), Order.class)
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
                        orderViewHolder.status_order.setText("Status : Delivered");
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
                        // go for rattings.
                        orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence choose[] = new CharSequence[]
                                        {
                                              "Rate this"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderlistActivity.this);
                                builder.setTitle("Options :");
                                builder.setItems(choose, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0)
                                        {
                                            Intent intent = new Intent(OrderlistActivity.this, GiveRatingActivity.class);
                                            intent.putExtra("pid", order.getPid());
                                            startActivity(intent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

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

    private void findAllOrder() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        FirebaseRecyclerOptions<Order> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(databaseReference.orderByChild("user").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone()), Order.class)
                        .build();
        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Order order) {
                        orderViewHolder.product_name_order.setText(order.getPname());
                        orderViewHolder.product_quantity_order.setText("Quantity : " + order.getQuantity());
                        orderViewHolder.product_price_per_order.setText("Price : RM " + order.getPrice());
                        float quantity = Float.parseFloat(order.getQuantity());
                        float perPrice = Float.parseFloat(order.getPrice());
                        float totalPrice = quantity * perPrice;
                        orderViewHolder.product_price_total_order.setText("Total : RM " + String.format("%.2f", totalPrice));
                        Picasso.get().load(order.getImage()).into(orderViewHolder.product_image_order);
                        orderViewHolder.status_order.setText("Status : "+ order.getStatus());
                        if(order.getStatus().equals("Delivered"))
                        {
                            orderViewHolder.status_order.setTextColor(Color.rgb(76, 175, 80));
                        }
                        if(order.getStatus().equals("Confirm"))
                        {
                            orderViewHolder.status_order.setTextColor(Color.rgb(255, 193, 7));
                        }
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
    // for showing all delivered order list.
    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String orderKey = ds.getKey();
                        final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone()).child(orderKey).child("Product");
                        FirebaseRecyclerOptions<Order> options =
                                new FirebaseRecyclerOptions.Builder<Order>()
                                        .setQuery(ProductsRef, Order.class)
                                        .build();

                        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Order order) {
                                        Toast.makeText(OrderlistActivity.this, "duck", Toast.LENGTH_SHORT).show();
//                                        orderViewHolder.productName.setText(order.getPname());
//                                        orderViewHolder.productQuantity.setText("Quantity : " + order.getQuantity());
//                                        orderViewHolder.productPrice.setText("Price : RM " + order.getPrice());
//                                        float quantity = Float.parseFloat(order.getQuantity());
//                                        float perPrice = Float.parseFloat(order.getPrice());
//                                        float totalPrice = quantity * perPrice;
//                                        orderViewHolder.totalPrice.setText("Total : RM " + String.format("%.2f", totalPrice));
//                                        Picasso.get().load(order.getImage()).into(orderViewHolder.productImage);
//                                        orderViewHolder.status.setText("Status : Not Shipped");
                                    }

                                    @NonNull
                                    @Override
                                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
                                        OrderViewHolder holder = new OrderViewHolder(view);
                                        return holder;
                                    }
                                };

                        recyclerView.setAdapter(adapter);
                        adapter.startListening();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    private void showDelivered() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    for (DataSnapshot ds : snapshot.getChildren())
//                    {
//                        String orderKey = ds.getKey();
//                        showfew(orderKey, "Delivered");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    private void showfew(String orderKey, final String delivered) {
//        final DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone()).child(orderKey).child("Product");
//        FirebaseRecyclerOptions<Order> options =
//                new FirebaseRecyclerOptions.Builder<Order>()
//                        .setQuery(ProductsRef.orderByChild("status").startAt(delivered).endAt(delivered), Order.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Order order) {
//
//                        orderViewHolder.productName.setText(order.getPname());
//                        orderViewHolder.productQuantity.setText("Quantity : " + order.getQuantity());
//                        orderViewHolder.productPrice.setText("Price : RM " + order.getPrice());
//                        float quantity = Float.parseFloat(order.getQuantity());
//                        float perPrice = Float.parseFloat(order.getPrice());
//                        float totalPrice = quantity * perPrice;
//                        orderViewHolder.totalPrice.setText("Total : RM " + String.format("%.2f", totalPrice));
//                        Picasso.get().load(order.getImage()).into(orderViewHolder.productImage);
//                        orderViewHolder.status.setText("Status : Delivered");
//                    }
//
//                    @NonNull
//                    @Override
//                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
//                        OrderViewHolder holder = new OrderViewHolder(view);
//                        return holder;
//                    }
//                };
//
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }

*/



}