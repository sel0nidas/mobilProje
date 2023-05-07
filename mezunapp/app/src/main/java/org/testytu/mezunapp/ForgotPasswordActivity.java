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

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView email = findViewById(R.id.email);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Button btnKayit = (Button) findViewById(R.id.btnKayit);

        Button btnGirisYonlendir = (Button) findViewById(R.id.btnGirisYonlendir);

        btnGirisYonlendir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnKayit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(task -> {
                   if(task.isSuccessful())
                       Toast.makeText(ForgotPasswordActivity.this, "E-Posta Gönderildi", Toast.LENGTH_SHORT).show();
                   else
                       Toast.makeText(ForgotPasswordActivity.this, "E-Posta Gönderilemedi", Toast.LENGTH_SHORT).show();
                });

            }
        });
    }
}