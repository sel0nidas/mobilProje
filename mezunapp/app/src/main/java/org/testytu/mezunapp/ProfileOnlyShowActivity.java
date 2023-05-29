package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ProfileOnlyShowActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_profile_onlyshow);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ImageView imageView = findViewById(R.id.profilePhoto);
        String userid = getIntent().getStringExtra("primaryId");
        StorageReference imageRef = storageRef.child("images/profilePhotos/"+userid);

        TextView adText = findViewById(R.id.adText);
        TextView soyadText = findViewById(R.id.soyadText);
        TextView girisYilText = findViewById(R.id.girisYilText);
        TextView mezunYilText = findViewById(R.id.mezunYilText);
        TextView emailText = findViewById(R.id.emailText);

        //Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        Button btnHome = (Button) findViewById(R.id.btnFriendRequest);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        DocumentReference userRef = db.collection("users").document(userid);



        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileOnlyShowActivity.this, UserSearchResultActivity.class);
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //Toast.makeText(LoginActivity.this, currentFirebaseUser.getUid().toString(), Toast.LENGTH_SHORT).show();
                intent.putExtra("primaryId", currentFirebaseUser.getUid());
                startActivity(intent);
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
                        Toast.makeText(ProfileOnlyShowActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //adText.setText("Selahattin");
    }
}