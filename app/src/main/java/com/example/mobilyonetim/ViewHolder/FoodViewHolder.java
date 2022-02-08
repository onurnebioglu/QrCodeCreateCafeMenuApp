package com.example.mobilyonetim.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilyonetim.Common.Common;
import com.example.mobilyonetim.Interface.ItemClickListener;
import com.example.mobilyonetim.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener //add for update/delete image
{

    public TextView foodName;
    public ImageView foodImage;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

        foodName = itemView.findViewById(R.id.food_name);
        foodImage = itemView.findViewById(R.id.food_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    //add for update/delete image
    //Override method for View.OnCreateContextMenuListener
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderIcon(R.drawable.firebase);
        menu.setHeaderTitle("İşlem Seçiniz");

        menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }
}
