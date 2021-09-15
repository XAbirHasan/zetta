package com.project.zetta.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Interface.ItemClickListner;
import com.project.zetta.R;

public class WishListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView productImage;
    public TextView productName, productQuantity, productPrice, totalPrice;
    private ItemClickListner itemClickListner;
    public Button addtoCart, delete;

    public WishListViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = (ImageView) itemView.findViewById(R.id.product_image1);
        productName = (TextView) itemView.findViewById(R.id.product_name1);
        productQuantity = (TextView) itemView.findViewById(R.id.product_quantity1);
        productPrice = (TextView) itemView.findViewById(R.id.product_price_per1);
        totalPrice = (TextView) itemView.findViewById(R.id.product_price_total1);
        delete = (Button) itemView.findViewById(R.id.delete_cart1);
        addtoCart = (Button) itemView.findViewById(R.id.add_to_cart1);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(itemView, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
