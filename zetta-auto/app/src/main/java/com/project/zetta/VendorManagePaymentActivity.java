package com.project.zetta;

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

public class VendorManagePaymentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView allOrder, delivered, noItem, whichType;
    private View allOrderView, deliveredView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_manage_payment);

        allOrder = (TextView)findViewById(R.id.allOrder);
        delivered = (TextView)findViewById(R.id.delivered);

        allOrderView = (View)findViewById(R.id.allOrderView);
        deliveredView = (View)findViewById(R.id.deliveredView);

        noItem = (TextView)findViewById(R.id.noItem);
        whichType = (TextView)findViewById(R.id.whichType);

        allOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allOrderView.setVisibility(View.VISIBLE);
                allOrder.setTextColor(Color.WHITE);

                delivered.setTextColor(Color.BLACK);
                deliveredView.clearAnimation();
                deliveredView.setVisibility(View.GONE);
                whichType.setText("Payment pending");
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
                whichType.setText("Payment done");
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

        if(whichType.getText().toString().equals("Payment pending"))
        {
            findAllOrder();
        }
        else if(whichType.getText().toString().equals("Payment done"))
        {
            findConfirm();
        }

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
                        orderViewHolder.status_order.setText("Payment : "+ "Paid");

                        orderViewHolder.status_order.setTextColor(Color.rgb(76, 175, 80));
                        noItem.setText("");
                        if(order.getStatus().equals("Not Shipped"))
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.GONE);
                        }
                        else
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.VISIBLE);
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

    private void findAllOrder() {
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
                        orderViewHolder.status_order.setText("Payment : "+ "Pending");

                        orderViewHolder.status_order.setTextColor(Color.rgb(255, 193, 7));

                        if(order.getStatus().equals("Not Shipped"))
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            orderViewHolder.relativeLayout.setVisibility(View.GONE);
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
}