package com.project.zetta.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Interface.ItemClickListner;
import com.project.zetta.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public ImageView productImage;
    public TextView productName, productQuantity, productPrice, totalPrice;
    private ItemClickListner itemClickListner;
    public Button edit, delete;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        productName = (TextView) itemView.findViewById(R.id.product_name);
        productQuantity = (TextView) itemView.findViewById(R.id.product_quantity);
        productPrice = (TextView) itemView.findViewById(R.id.product_price_per);
        totalPrice = (TextView) itemView.findViewById(R.id.product_price_total);
        edit = (Button) itemView.findViewById(R.id.edit_cart);
        delete = (Button) itemView.findViewById(R.id.delete_cart);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(itemView, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
