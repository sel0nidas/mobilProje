package org.testytu.mezunapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ActivityResultLauncher<String> pickImageLauncher;

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
        setContentView(R.layout.activity_profile);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ImageView imageView = findViewById(R.id.profilePhoto);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = currentFirebaseUser.getUid();
        StorageReference imageRef = storageRef.child("images/profilePhotos/"+userid);

        TextView adText = findViewById(R.id.adText);
        TextView soyadText = findViewById(R.id.soyadText);
        TextView girisYilText = findViewById(R.id.girisYilText);
        TextView mezunYilText = findViewById(R.id.mezunYilText);
        TextView emailText = findViewById(R.id.emailText);

        Button btnGallery = (Button) findViewById(R.id.btnGallery);
        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        Button btnHome = (Button) findViewById(R.id.btnHome);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        DocumentReference userRef = db.collection("users").document(userid);


        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the picked image here
                        imageView.setImageURI(uri);
                        imageRef.putFile(uri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Handle successful upload
                                        Toast.makeText(ProfileActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
                                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {

                                                String imageURL = uri2.toString();

                                                Map<String, Object> data = new HashMap<>();
                                                data.put("profilePhoto", imageURL);

                                                userRef.set(data, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                Toast.makeText(ProfileActivity.this, imageURL, Toast.LENGTH_SHORT).show();
                                                // Do something with the URL, such as display it in an ImageView
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle any errors
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failed upload
                                        Toast.makeText(ProfileActivity.this, "Not Successful :(", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageLauncher.launch("image/*");
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //Toast.makeText(LoginActivity.this, currentFirebaseUser.getUid().toString(), Toast.LENGTH_SHORT).show();
                intent.putExtra("primaryId", currentFirebaseUser.getUid());
                startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> studentValues = new HashMap<>();
                studentValues.put("ad", adText.getText().toString());
                studentValues.put("soyad", soyadText.getText().toString());
                studentValues.put("girisYil", girisYilText.getText().toString());
                studentValues.put("mezunYil", mezunYilText.getText().toString());
                studentValues.put("email", emailText.getText().toString());

                userRef.set(studentValues, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Profil Bilgilerin Başarıyla Güncellendi", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Profil Bilgileri Güncellenirken Sorun Oluştu", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();
        usersRef.document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        String ad = document.getString("ad");
                        String soyad = document.getString("soyad");
                        String girisYil = document.getString("girisYil");
                        String mezunYil = document.getString("mezunYil");
                        String email = document.getString("email");
                        String url = document.getString("profilePhoto");

                        //Toast.makeText(ProfileActivity.this, url, Toast.LENGTH_SHORT).show();

                        adText.setText(ad);
                        soyadText.setText(soyad);
                        girisYilText.setText(girisYil);
                        mezunYilText.setText(mezunYil);
                        emailText.setText(email);
                        if(!url.isEmpty())
                            Picasso.get().load(url).into(imageView);

                        //Toast.makeText(ProfileActivity.this, ad, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //adText.setText("Selahattin");
    }
}