package com.example.mobilyonetim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.mobilyonetim.Common.Common;
import com.example.mobilyonetim.Model.User;

public class SignIn extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button buttonSignIn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPhone = findViewById(R.id.edit_Phone);
        editPassword = findViewById(R.id.edit_Password);
        buttonSignIn = findViewById(R.id.button_signin);

        //Firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(editPhone.getText().toString(), editPassword.getText().toString());
            }
        });
    }

    //signInUser() method
    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Lütfen Bekleyiniz...");
        mDialog.show();

        final String localPhone = phone;
        final String localPassword = password;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists()){
                    mDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsStaff())){ //isStaff == true kullanıcının yönetici olması kontrolü
                        if(user.getPassword().equals(localPassword)){
                            Intent intentHome = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(intentHome);
                            finish();
                        }
                        else{
                            Toast.makeText(SignIn.this, "Hatalı şifre!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignIn.this, "Lütfen yönetici hesabıyla giriş yapınız!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "Kullanıcı bulunamadı!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
