package com.example.mobilyonetim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.mobilyonetim.Common.Common;
import com.example.mobilyonetim.Interface.ItemClickListener;
import com.example.mobilyonetim.Model.Category;
import com.example.mobilyonetim.Model.Food;
import com.example.mobilyonetim.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    FloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //Yeni ürün ekleme
    EditText editName, editDescription, editPrice;
    Button btnSelect, btnUpload;

    Food newFood;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Foods");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Init
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.root_Layout);

        fab = findViewById(R.id.fab_foodList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FoodList.this, "FoodList" + this.getClass().getName(), Toast.LENGTH_SHORT).show();
                showAddFoodDialog();
            }
        });

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty()) {
            loadListFood(categoryId);
        }

    }

    //showAddFoodDialog() method
    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Yeni ürün ekle");
        alertDialog.setMessage("Lütfen tüm bilgileri doldurun");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null);

        editName = add_menu_layout.findViewById(R.id.edit_name_anf);
        editDescription = add_menu_layout.findViewById(R.id.edit_description_anf);
        editPrice = add_menu_layout.findViewById(R.id.edit_price_anf);

        btnSelect = add_menu_layout.findViewById(R.id.btn_select_anf);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload_anf);


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //Copy from HomeActivity
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(); //Copy from HomeActivity
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.shopping_cart);

        //setButton
        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Yeni kategori oluşturma
                if (newFood != null) {
                    foodList.push().setValue(newFood);
                    Snackbar.make(rootLayout, "Yeni Kategori " + newFood.getName() + " eklendi", Snackbar.LENGTH_SHORT).show();
                }
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

    //Resim seçme methodu
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), Common.PIC_IMAGE_REQUEST);
    }

    //Seçilen resmi yükleme methodu
    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();

            //Resim için RANDOM UUID üretilerek images klasörüne kayıt
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Yükleme Başarılı!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Yeni kategori için değerler oluşturma
                                    newFood = new Food();
                                    newFood.setName(editName.getText().toString());
                                    newFood.setDescription(editDescription.getText().toString());
                                    newFood.setPrice(editPrice.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Hata : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Yüklendi: " + progress + "%");
                        }
                    });
        }
    }

    //Ürün listesini çekmek
    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.foodName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.foodImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PIC_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            saveUri = data.getData();
            btnSelect.setText("Img Seçildi");
        }
    }


    //Silme ve güncelleme işlemlerini seçmek için method
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE)) {

            showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals(Common.DELETE)) {

            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    //Silme methodu
    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    //Resim değiştirme methodu
    private void showUpdateFoodDialog(final String key, final Food item) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Ürün Düzenle");
        alertDialog.setMessage("Lütfen tüm bilgileri doldurun");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null);

        editName = add_menu_layout.findViewById(R.id.edit_name_anf);
        editDescription = add_menu_layout.findViewById(R.id.edit_description_anf);
        editPrice = add_menu_layout.findViewById(R.id.edit_price_anf);

        //Varsayılan görseli set etme
        editName.setText(item.getName());
        editDescription.setText(item.getDescription());
        editPrice.setText(item.getPrice());

        btnSelect = add_menu_layout.findViewById(R.id.btn_select_anf);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload_anf);



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.shopping_cart);


        alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Güncel bilgileri set etme
                item.setName(editName.getText().toString());
                item.setDescription(editDescription.getText().toString());
                item.setPrice(editPrice.getText().toString());

                foodList.child(key).setValue(item);

                Snackbar.make(rootLayout, "Category " + item.getName() + " Düzenleme Başarılı", Snackbar.LENGTH_SHORT).show();

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


    //Resim Değiştirme methodu
    private void changeImage(final Food item) {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Yükleniyor...");
            mDialog.show();

            //Rastgele isim id si oluşturma
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Yükleme Başarılı!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Değerleri yükleyerek yeni resim indirme bağlantısını set etme
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Hata : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Yüklendi: " + progress + "%");
                        }
                    });
        }
    }
}