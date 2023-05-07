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
import com.google.firebase.firestore.auth.User;

public class LoginActivity extends AppCompatActivity {

    /*
    private String ad = "";
    private String soyad = "";
    private int girisYil = 0;
    private int mezunYil = 0;
    private String email = "";
    private String password = "";
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        /*
        if(currentFirebaseUser.getUid() != null){
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra("primaryId", currentFirebaseUser.getUid());
            startActivity(intent);
        }
        */
        Button btnKayit = (Button) findViewById(R.id.btnKayit);

        Button btnForgotPassword = (Button) findViewById(R.id.btnForgotPassword);

        Button btnKayitYonlendir = (Button) findViewById(R.id.btnGirisYonlendir);

        btnKayitYonlendir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnKayit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                //Toast.makeText(LoginActivity.this, currentFirebaseUser.getUid().toString(), Toast.LENGTH_SHORT).show();
                                intent.putExtra("primaryId", currentFirebaseUser.getUid());
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}