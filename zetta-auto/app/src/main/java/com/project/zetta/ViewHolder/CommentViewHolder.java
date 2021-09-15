package com.project.zetta.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Interface.ItemClickListner;
import com.project.zetta.R;


public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView product_comments;
    public RatingBar ratingBarView;
    public ItemClickListner itemClickListner;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        product_comments = (TextView)itemView.findViewById(R.id.product_comments);
        ratingBarView = (RatingBar)itemView.findViewById(R.id.ratingBarView);
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
