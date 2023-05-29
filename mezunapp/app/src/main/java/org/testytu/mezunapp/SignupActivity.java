package org.testytu.mezunapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        TextView ad = findViewById(R.id.ad);
        TextView soyad = findViewById(R.id.soyad);
        TextView girisYil = findViewById(R.id.girisYil);
        TextView mezunYil = findViewById(R.id.mezunYil);
        TextView email = findViewById(R.id.email);
        TextView password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button btnKayit = (Button) findViewById(R.id.btnKayit);

        Button btnGirisYonlendir = (Button) findViewById(R.id.btnGirisYonlendir);

        btnGirisYonlendir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnKayit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Student std = new Student("", ad.getText().toString(), soyad.getText().toString(), girisYil.getText().toString(), mezunYil.getText().toString(), "");
                //Toast.makeText(MainActivity.this, ad.getText().toString(), Toast.LENGTH_SHORT).show();
                HashMap<String, Object> studentValues = new HashMap<>();
                studentValues.put("ad", std.getAd());
                studentValues.put("soyad", std.getSoyad());
                studentValues.put("girisYil", std.getGirisYil());
                studentValues.put("mezunYil", std.getMezunYil());
                studentValues.put("email", email.getText().toString());
                studentValues.put("phoneNumber", "");
                studentValues.put("bolum", "");
                studentValues.put("profilePhoto", "https://t3.ftcdn.net/jpg/03/42/99/68/360_F_342996846_tHMepJOsXWwbvMpG7uiYpE68wbfQ9e4s.jpg");

                //Toast.makeText(MainActivity.this, mAuth.toString(), Toast.LENGTH_SHORT).show();

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Kayit Basarili", Toast.LENGTH_SHORT).show();

                                    String userId = mAuth.getCurrentUser().getUid();
                                    db.collection("users").document(userId).set(studentValues)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        Toast.makeText(SignupActivity.this, "Signup is successful, please check email for verification", Toast.LENGTH_SHORT).show();
                                                                        // Email sent successfully
                                                                        // Show a message to the user or redirect to a verification screen
                                                                    } else {
                                                                        // Email sending failed
                                                                        // Handle the error
                                                                        Toast.makeText(SignupActivity.this, "Signup is successful but failure on verification", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }
                                                            });

                                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                    startActivity(intent);


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                                else
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}