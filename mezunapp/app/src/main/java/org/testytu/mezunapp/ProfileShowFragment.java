package org.testytu.mezunapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileShowFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileShowFragment() {
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
    public static ProfileShowFragment newInstance(String param1, String param2) {
        ProfileShowFragment fragment = new ProfileShowFragment();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    private void openWhatsApp(String phoneNumber, String message) {
        String formattedMessage = Uri.encode(message);
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + formattedMessage);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void openMailApp(String email, String subject) {

        Toast.makeText(getActivity(), "Redirecting to the mail app", Toast.LENGTH_SHORT).show();

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + email));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Perform Firestore query here

        String userid = getArguments().getString("primaryId");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ImageView imageView = view.findViewById(R.id.profilePhoto);
        //String userid = getIntent().getStringExtra("primaryId");
        //String userid2 = "D01CYu20ARXRJx0Sp1QwCi1GkDO2";

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid2 = currentFirebaseUser.getUid();

        StorageReference imageRef = storageRef.child("images/profilePhotos/"+userid);

        TextView adText = view.findViewById(R.id.adText);
        TextView soyadText = view.findViewById(R.id.soyadText);
        TextView girisYilText = view.findViewById(R.id.girisYilText);
        TextView mezunYilText = view.findViewById(R.id.mezunYilText);
        TextView emailText = view.findViewById(R.id.emailText);
        TextView phoneText = view.findViewById(R.id.PhoneText);

        Button btnFriendRequest = (Button) view.findViewById(R.id.btnFriendRequest);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        DocumentReference userRef = db.collection("users").document(userid);


        Button MailButton = view.findViewById(R.id.mailBtn);
        Button WhatsappButton = view.findViewById(R.id.whatsappBtn);

        MailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the mail app with a pre-filled email
                openMailApp(emailText.getText().toString(),"İletişim");
            }
        });
        WhatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp("+905459293976", "Selam!");
            }
        });

        btnFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Define the sender, receiver, and situation values
                String senderId = userid2;
                String receiverId = userid;
                String situation = "Waiting"; // Replace with the desired situation value

                // Create a new document in the "relationships" collection
                DocumentReference relationshipRef = db.collection("friendshiprequests").document();

                // Create a data object to hold the relationship details
                Map<String, Object> relationshipData = new HashMap<>();
                relationshipData.put("sender", senderId);
                relationshipData.put("receiver", receiverId);
                relationshipData.put("situation", situation);

                // Set the data on the document
                relationshipRef.set(relationshipData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Friend Request Successfully Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error occurred while creating the document
                                // Handle the error accordingly
                                Toast.makeText(getActivity(), "Error: Request is not sent", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                        String phone = document.getString("phone");
                        //Toast.makeText(ProfileActivity.this, url, Toast.LENGTH_SHORT).show();

                        adText.setText(ad);
                        soyadText.setText(soyad);
                        girisYilText.setText(girisYil);
                        mezunYilText.setText(mezunYil);
                        emailText.setText(email);
                        phoneText.setText(phone);
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
        return inflater.inflate(R.layout.activity_profile_onlyshow, container, false);
    }
}