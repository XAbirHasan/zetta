package com.project.zetta.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.zetta.Interface.ItemClickListner;
import com.project.zetta.R;

public class BoyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView boy_name, boy_phone;
    public de.hdodenhof.circleimageview.CircleImageView boyProfile;
    private ItemClickListner itemClickListner;

    public BoyViewHolder(@NonNull View itemView) {
        super(itemView);
        boy_name = (TextView)itemView.findViewById(R.id.boy_name);
        boy_phone = (TextView)itemView.findViewById(R.id.boy_phone);
        boyProfile = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.boyProfile);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(itemView, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
