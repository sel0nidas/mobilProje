package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserSearchResultActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    private DatabaseReference mDatabase;
    private ActivityResultLauncher<String> pickImageLauncher;

    RecyclerView recyclerViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);

        recyclerViewUsers = findViewById(R.id.userRecycler);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query usersQuery = db.collection("users");
        CollectionReference usersRef = db.collection("users");


        usersQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Student> stdList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String id = documentSnapshot.getId();
                    String ad = documentSnapshot.getString("ad").toString();
                    String soyad = documentSnapshot.getString("soyad").toString();
                    String girisYil = documentSnapshot.getString("girisYil").toString();
                    String mezunYil = documentSnapshot.getString("mezunYil").toString();
                    String profilePhoto = documentSnapshot.getString("profilePhoto").toString();

                    Student std = new Student(id, ad, soyad, girisYil, mezunYil, profilePhoto);
                    stdList.add(std);

                    Toast.makeText(UserSearchResultActivity.this, "AAA", Toast.LENGTH_SHORT).show();
                }

                MyAdapter userAdapter = new MyAdapter(stdList);
                recyclerViewUsers.setLayoutManager(new LinearLayoutManager(UserSearchResultActivity.this));
                recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());

                userAdapter.setOnItemClickListener(UserSearchResultActivity.this);
                recyclerViewUsers.setAdapter(userAdapter);
            }
        });
    }
    // Implement the onItemClick() method to start a new activity to display the user's profile
    @Override
    public void onItemClick(Student item) {
        //Toast.makeText(this, item.getAd(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProfileOnlyShowActivity.class);
        intent.putExtra("primaryId", item.getId());
        startActivity(intent);
    }
}