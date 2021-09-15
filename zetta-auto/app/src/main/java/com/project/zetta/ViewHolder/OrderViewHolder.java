package com.project.zetta.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Interface.ItemClickListner;
import com.project.zetta.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ItemClickListner itemClickListner;
    public ImageView product_image_order;
    public TextView product_name_order, product_quantity_order, product_price_per_order, product_price_total_order, status_order;
    public View view;
    public RelativeLayout relativeLayout;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        product_image_order = (ImageView)itemView.findViewById(R.id.product_image_order);
        product_name_order = (TextView)itemView.findViewById(R.id.product_name_order);
        product_quantity_order = (TextView)itemView.findViewById(R.id.product_quantity_order);
        product_price_per_order = (TextView)itemView.findViewById(R.id.product_price_per_order);
        product_price_total_order = (TextView)itemView.findViewById(R.id.product_price_total_order);
        status_order = (TextView)itemView.findViewById(R.id.status_order);
        relativeLayout = (RelativeLayout)itemView.findViewById(R.id.wholeLayout);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.itemClickListner = listner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(itemView, getAdapterPosition(), false);
    }
}
