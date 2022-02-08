package com.example.mobilyonetim.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mobilyonetim.R;

public class DetailViewHolder extends RecyclerView.ViewHolder{

    public TextView produName, quantity, price;



    public DetailViewHolder(View itemView) {
        super(itemView);

        produName = itemView.findViewById(R.id.produ_name);
        quantity = itemView.findViewById(R.id.quantity_txt);
        price=itemView.findViewById(R.id.produ_price);




    }


}
