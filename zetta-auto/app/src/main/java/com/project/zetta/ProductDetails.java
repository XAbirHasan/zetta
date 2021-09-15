package com.project.zetta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Model.Product;
import com.project.zetta.Model.Ratings;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.CommentViewHolder;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProductDetails extends AppCompatActivity {

    private Button addToCartBtn, addToWishlistBtn;
    private TextView productName, productDescription, productPrice, sumRating, product_origin, product_warrenty, product_availble, product_availble_calculate;
    private ImageView productImage;
    private com.cepheuen.elegantnumberbutton.view.ElegantNumberButton numberButton;
    private String productID = "";
    private String getProductKey = "";
    private String imgUrl = "";
    private int remain = 0;
    private TextView product_seller;


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        getProductKey = getIntent().getStringExtra("key");

        addToCartBtn = (Button)findViewById(R.id.product_add_to_cart);
        addToWishlistBtn = (Button)findViewById(R.id.product_add_to_wishlist);

        productName = (TextView)findViewById(R.id.product_name);
        productDescription = (TextView)findViewById(R.id.product_description);
        productPrice = (TextView)findViewById(R.id.product_price);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        sumRating = (TextView)findViewById(R.id.sumRating);
        productImage = (ImageView) findViewById(R.id.product_image);
        numberButton = (com.cepheuen.elegantnumberbutton.view.ElegantNumberButton)findViewById(R.id.product_quantity);
        getProductDetails(productID);


        //product_origin, product_warrenty, product_availble
        product_origin = (TextView)findViewById(R.id.product_origin);
        product_warrenty = (TextView)findViewById(R.id.product_warrenty);
        product_availble = (TextView)findViewById(R.id.product_availble);
        product_availble_calculate = (TextView)findViewById(R.id.product_availble_calculate);

        product_seller = (TextView)findViewById(R.id.product_seller);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addCart = Integer.parseInt(product_availble_calculate.getText().toString()) - Integer.parseInt(numberButton.getNumber());
                if(addCart < 0)
                {
                    Toast.makeText(ProductDetails.this, "Exceed Available Quantity !", Toast.LENGTH_SHORT).show();
                }
                else addToCartlist();
            }
        });

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int addCart = Integer.parseInt(product_availble_calculate.getText().toString()) - Integer.parseInt(numberButton.getNumber());
                if(addCart < 0)
                {
                    Toast.makeText(ProductDetails.this, "Exceed Available Quantity !", Toast.LENGTH_SHORT).show();
                }
                else addToWishList();

            }
        });


        // this is for ratting and comments
        // never forget to add this it took 2hours to find what is the problem.
        recyclerView = findViewById(R.id.allcomments);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings");
        FirebaseRecyclerOptions<Ratings> options = new
                FirebaseRecyclerOptions.Builder<Ratings>().setQuery(databaseReference.child(productID), Ratings.class).build();
        FirebaseRecyclerAdapter<Ratings, CommentViewHolder> adapter =
                new FirebaseRecyclerAdapter<Ratings, CommentViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i, @NonNull Ratings ratings) {
                        commentViewHolder.product_comments.setText(ratings.getComment());
                        commentViewHolder.ratingBarView.setRating(2);
                    }

                    @NonNull
                    @Override
                    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view_holder, parent, false);
                        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
                        return commentViewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void addToWishList()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Wish list");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("image", imgUrl);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("status", "Not Shipped");
        cartMap.put("seller", product_seller.getText().toString());
        cartMap.put("user", Prevalent.currentOnlineUser.getPhone());

        // for randomkey

        String productRandomKey = getProductKey;


        if(getProductKey.equals("None"))
        {
            // for randomkey
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
        }

        cartMap.put("key", productRandomKey);


        cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Product").child(productRandomKey).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProductDetails.this, "Added to Wish list", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ProductDetails.this, CustomerHomeActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void addToCartlist()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart list");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("image", imgUrl);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("status", "Not Shipped");
        cartMap.put("seller", product_seller.getText().toString());
        cartMap.put("user", Prevalent.currentOnlineUser.getPhone());

        String productRandomKey = getProductKey;


        if(getProductKey.equals("None"))
        {
            // for randomkey
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
        }
        cartMap.put("key", productRandomKey);

        cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Product").child(productRandomKey).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProductDetails.this, "Added to cart list", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ProductDetails.this, CustomerHomeActivity.class);
//                            startActivity(intent);
                            finish();
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
                    int all, done;
                    all = Integer.parseInt(product.getQuantity());
                    done = Integer.parseInt(product.getSold());
                    remain = all - done;
                    product_availble.setText("Avaible : " + String.valueOf(remain));
                    product_availble_calculate.setText(String.valueOf(remain));
                    product_seller.setText(product.getSeller());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(productID);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                int sum = 0;
                                int c = 0;
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                    Object ratings = map.get("rating");
                                    int val = Integer.parseInt(String.valueOf(ratings));
                                    c += 1;
                                    sum += val;
                                    float avgrating = (float) sum / c;
                                    String rating = String.format("%.2f", avgrating);
                                    ratingBar.setRating(Float.parseFloat(rating));
                                    sumRating.setText("Ratings(" + String.valueOf(c) + ")");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}