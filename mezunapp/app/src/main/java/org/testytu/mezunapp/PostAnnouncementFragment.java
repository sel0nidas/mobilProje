package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
 * Use the {@link PostAnnouncementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostAnnouncementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostAnnouncementFragment() {
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
    public static PostAnnouncementFragment newInstance(String param1, String param2) {
        PostAnnouncementFragment fragment = new PostAnnouncementFragment();
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

        EditText title = view.findViewById(R.id.postTitle);
        EditText text = view.findViewById(R.id.postText);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button btnPost = (Button) view.findViewById(R.id.buttonPost);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Define the sender, receiver, and situation values
                String senderId = "asd";
                String receiverId = "asd";
                String situation = "Waiting"; // Replace with the desired situation value

                // Create a new document in the "relationships" collection
                DocumentReference relationshipRef = db.collection("announcements").document();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_announcement, container, false);
    }
}