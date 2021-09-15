package com.project.zetta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Model.Product;
import com.project.zetta.Prevalent.Prevalent;
import com.project.zetta.ViewHolder.ProductViewHolder;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class CustomerHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView sumVal, countVal;

    private EditText search_product;
    private Button searchBtn;
    private String searchWord;
    private TextView notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to cart..
                Intent intent = new Intent(CustomerHomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // this if for search
        search_product = (EditText)findViewById(R.id.search_product);
        searchBtn =  (Button)findViewById(R.id.searchBtn);
        notfound = (TextView)findViewById(R.id.notfound);
        notfound.setText("Product not found");



        /// this is for image
        com.denzcoskun.imageslider.ImageSlider imageSlider = (com.denzcoskun.imageslider.ImageSlider)findViewById(R.id.image_slider);
        List<SlideModel> slideModelList = new ArrayList<>();
        slideModelList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/e-commerce-10e5b.appspot.com/o/Product%20Images%2Fimage%3A25867430Jan%2010%2C%20202115%3A33%3A04%20PM.jpg?alt=media&token=93abaa6d-b61d-4d86-9e9c-763766b92bef", "Car"));
        slideModelList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/e-commerce-10e5b.appspot.com/o/Product%20Images%2F25866964Jan%2010%2C%20202113%3A56%3A15%20PM.jpg?alt=media&token=8c19dc63-8208-4d1b-8db6-15851b841b45", "Light"));
        slideModelList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/e-commerce-10e5b.appspot.com/o/Product%20Images%2Fimage%3A25864Dec%2027%2C%20202006%3A31%3A58%20AM.jpg?alt=media&token=a4269046-2f51-4cef-b5d4-2c3630a1dd9c", "Headlight"));
        slideModelList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/e-commerce-10e5b.appspot.com/o/Product%20Images%2Fimage%3A35817Jan%2012%2C%20202113%3A47%3A08%20PM.jpg?alt=media&token=0b4345a6-6a1b-4593-a81e-97464d515b41", "Bettary"));
        imageSlider.setImageList(slideModelList, true);
        /// image end

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWord = search_product.getText().toString();
                notfound.setText("Product not found");
                onStart();

            }
        });


        // firebase
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // here the nav bar click implemented
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        sumVal = (TextView)findViewById(R.id.sum);
        countVal = (TextView)findViewById(R.id.count);

        // for logout
        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CustomerHomeActivity.this, logout.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        // go to profile setting
        navigationView.getMenu().findItem(R.id.nav_setting).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CustomerHomeActivity.this, Settings.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        // go to cart
        navigationView.getMenu().findItem(R.id.nav_cart).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CustomerHomeActivity.this, CartActivity.class);
                startActivity(intent);
                return false;
            }
        });

        // go to wishlist
        navigationView.getMenu().findItem(R.id.nav_wishlist).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CustomerHomeActivity.this, WishlistActivity.class);
                startActivity(intent);
                return false;
            }
        });


        // go to order
        navigationView.getMenu().findItem(R.id.nav_order).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CustomerHomeActivity.this, OrderlistActivity.class);
                startActivity(intent);
                return false;
            }
        });



        // init
        Paper.init(this);

        // set name and profile pic
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.user_profile_name);
        de.hdodenhof.circleimageview.CircleImageView userImg = headerView.findViewById(R.id.user_profile_image);
        username.setText(Prevalent.currentOnlineUser.getName());

        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(userImg);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(ProductsRef.orderByChild("pname").startAt(searchWord).endAt(searchWord+"\uf8ff"), Product.class)
                        .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Product model)
                    {
                        holder.txtProductName.setText(model.getPname());

                        // for safety set to 0;
                        sumVal.setText("0");
                        countVal.setText("1");

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings").child(model.getPid());
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
                                        holder.ratingBar.setRating(Float.parseFloat(rating));
                                        holder.sumRating.setText("Ratings(" + String.valueOf(c) + ")");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        holder.txtProductPrice.setText("Price : RM " + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CustomerHomeActivity.this, ProductDetails.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("key", "None");
                                startActivity(intent);

                            }
                        });

                        sumVal.setText("");
                        countVal.setText("");
                        notfound.setText("");
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_smaller, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(CustomerHomeActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Exit");

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

                                // When the user click yes button
                                // then app will close
                                finish();
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
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}