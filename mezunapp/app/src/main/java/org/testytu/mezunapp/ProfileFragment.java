package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Perform Firestore query here

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ImageView imageView = view.findViewById(R.id.profilePhoto);
        //String userid = getIntent().getStringExtra("primaryId");
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = currentFirebaseUser.getUid();
        //String userid = "D01CYu20ARXRJx0Sp1QwCi1GkDO2";
        StorageReference imageRef = storageRef.child("images/profilePhotos/"+userid);

        TextView adText = view.findViewById(R.id.adText);
        TextView soyadText = view.findViewById(R.id.soyadText);
        TextView girisYilText = view.findViewById(R.id.girisYilText);
        TextView mezunYilText = view.findViewById(R.id.mezunYilText);
        TextView emailText = view.findViewById(R.id.emailText);
        TextView phoneText = view.findViewById(R.id.phoneText);
        TextView bolumText = view.findViewById(R.id.bolumText);

        Button btnGoUpdate = (Button) view.findViewById(R.id.btnUpdate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        DocumentReference userRef = db.collection("users").document(userid);




        btnGoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kaldır ve kullanıcıyı currentUser olarak al
                String userid = "D01CYu20ARXRJx0Sp1QwCi1GkDO2";
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("primaryId", userid);
                startActivity(intent);
            }
        });

        Toast.makeText(getActivity(), userid, Toast.LENGTH_SHORT).show();
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
                        String phoneNumber = document.getString("phoneNumber");
                        String bolum = document.getString("bolum");


                        //Toast.makeText(ProfileActivity.this, url, Toast.LENGTH_SHORT).show();

                        adText.setText(ad);
                        soyadText.setText(soyad);
                        girisYilText.setText(girisYil);
                        mezunYilText.setText(mezunYil);
                        emailText.setText(email);
                        phoneText.setText(phoneNumber);
                        bolumText.setText(bolum);
                        if(!url.isEmpty())
                            Picasso.get().load(url).into(imageView);

                        //Toast.makeText(ProfileActivity.this, ad, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "No Results Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //adText.setText("Selahattin");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile2, container, false);
    }
}