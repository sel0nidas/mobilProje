package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Button btnProfile = (Button) findViewById(R.id.btnProfile);
        Button btnDuyuruBak = (Button) findViewById(R.id.btnDuyuruBak);
        Button btnDuyuruEkle = (Button) findViewById(R.id.btnDuyuru);
        Button btnKullaniciAra = (Button) findViewById(R.id.btnKullaniciAra);

        btnProfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnDuyuruBak.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, UserSearchActivity.class);
                startActivity(intent);
            }
        });

        btnDuyuruEkle.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, UserSearchActivity.class);
                startActivity(intent);
            }
        });

        btnKullaniciAra.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, UserSearchResultActivity.class);
                startActivity(intent);
            }
        });

    }
}