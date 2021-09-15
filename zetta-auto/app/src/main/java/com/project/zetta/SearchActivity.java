package com.project.zetta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.zetta.Model.Product;
import com.project.zetta.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private EditText searchKeyWord;
    private Button searchBtn;
    private androidx.recyclerview.widget.RecyclerView searchList;
    private String searchWord;
    private DatabaseReference ProductsRef;
    private TextView notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchKeyWord = (EditText)findViewById(R.id.search_product);
        searchBtn =  (Button)findViewById(R.id.searchBtn);
        searchList = (androidx.recyclerview.widget.RecyclerView)findViewById(R.id.recycler_menu);
        searchList.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        notfound = (TextView)findViewById(R.id.notfound);
        notfound.setText("Product not found");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWord = searchKeyWord.getText().toString();
                notfound.setText("Product not found");
                onStart();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // firebase
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(ProductsRef.orderByChild("pname").startAt(searchWord).endAt(searchWord+"\uf8ff"), Product.class)
                        .build();


        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Product model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price : RM " + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchActivity.this, ProductDetails.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("key", "None");
                                startActivity(intent);

                            }
                        });

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
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}