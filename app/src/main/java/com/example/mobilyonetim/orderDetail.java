package com.example.mobilyonetim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import com.example.mobilyonetim.Model.Detail;
import com.example.mobilyonetim.Model.Food;
import com.example.mobilyonetim.ViewHolder.DetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class orderDetail extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;


    //Firebase Database
    FirebaseDatabase database;
    DatabaseReference details;
    private Button ready, served;




    FirebaseRecyclerAdapter<Detail, DetailViewHolder> adapter;

    public orderDetail() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ready=findViewById(R.id.btnReady);
        served=findViewById(R.id.btnServed);

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateFoodDialog();
            }
        });
        served.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateFoodDialog2();
            }
        });

        //Firebase
        database = FirebaseDatabase.getInstance();
        details = database.getReference("Requests");

        recyclerView = findViewById(R.id.listDetail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Sipariş numarasına göre detayları listeleme
        loadDetails(getIntent().getStringExtra("OrderId"));

    }

    private void loadDetails(String id) {
        adapter = new FirebaseRecyclerAdapter<Detail, DetailViewHolder>(
                Detail.class,
                R.layout.detail_layout,
                DetailViewHolder.class,

                details.child(id).child("foods")

        ) {
            @Override
            protected void populateViewHolder(DetailViewHolder viewHolder, Detail model, int position) {
                viewHolder.produName.setText(model.getProduName());
                viewHolder.quantity.setText(model.getQuantity()+" adet");
                viewHolder.price.setText(model.getPrice()+" TL");


            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void showUpdateFoodDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderDetail.this);
        alertDialog.setTitle("Sipariş");
        alertDialog.setMessage("Sipariş durumu hazırlanıyor olarak değiştirilsin mi ?");



        //setButton
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String id=getIntent().getStringExtra("OrderId");


                details.child(id).child("status").setValue("1");



            }
        });

        alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private void showUpdateFoodDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(orderDetail.this);
        alertDialog.setTitle("Sipariş");
        alertDialog.setMessage("Sipariş durumu servis edildi olarak değiştirilsin mi ?");



        //setButton
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                String id=getIntent().getStringExtra("OrderId");


                details.child(id).child("status").setValue("2");


            }
        });

        alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }



}
