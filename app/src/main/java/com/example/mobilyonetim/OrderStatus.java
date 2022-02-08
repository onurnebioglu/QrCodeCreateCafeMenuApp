package com.example.mobilyonetim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilyonetim.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.example.mobilyonetim.Model.Request;
import com.example.mobilyonetim.ViewHolder.OrderViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;


    //Firebase Database
    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txt;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Fitebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");


        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //I change the value from
        loadOrders();
    }

    //loadOrders() method
    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,

                requests

        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getTable());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderTotal.setText(model.getTotal());
                viewHolder.txtOrderStatus.setBackgroundResource(convertCodeToColor(model.getStatus()));


                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent detail = new Intent(OrderStatus.this,orderDetail.class);
                        detail.putExtra("OrderId",adapter.getRef(position).getKey());
                        Toast.makeText(OrderStatus.this, "Sipariş Gösterildi ", Toast.LENGTH_SHORT).show();
                        startActivity(detail);



                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }


    private String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Alındı";
        }
        else if (status.equals("1"))
        {
            return "Hazırlanıyor";
        }
        else  {
            return "Servis Edildi";
        }
    }
    private int convertCodeToColor(String status) {
        OrderViewHolder viewHolder;
        if (status.equals("0")){




            return R.drawable.button_background_2;
        }
        if (status.equals("1"))
        {
            return R.drawable.button_background_4;

        }
        else {
            return R.drawable.button_background_3;
        }

    }
}
