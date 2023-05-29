package org.testytu.mezunapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendRequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestsFragment extends Fragment implements FriendRequestAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendRequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static FriendRequestsFragment newInstance(String param1, String param2) {
        FriendRequestsFragment fragment = new FriendRequestsFragment();
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
            String entranceYear = getArguments().getString(ARG_PARAM1);
            String graduationYear = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = currentFirebaseUser.getUid();
        //String userid = "D01CYu20ARXRJx0Sp1QwCi1GkDO2";
        //String userid = "N6PD51JhQkNyjHBRVSX9LiMtyWj2";
        //String senderID = "N6PD51JhQkNyjHBRVSX9LiMtyWj2";
        DatabaseReference mDatabase;
        ActivityResultLauncher<String> pickImageLauncher;

        RecyclerView recyclerViewUsers = view.findViewById(R.id.userRecycler);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query usersQuery = db.collection("friendshiprequests");
        CollectionReference usersRef = db.collection("users");

        // Start with the base query
        Query friendRequestsQuery = usersQuery.whereEqualTo("situation", "Waiting").whereEqualTo("receiver", userid);
        friendRequestsQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot friendRequestsSnapshot) {
                        ArrayList<Student> stdList = new ArrayList<>();
                        for (QueryDocumentSnapshot friendRequestDocument : friendRequestsSnapshot) {
                            String senderId = friendRequestDocument.getString("sender");

                            // Retrieve sender's profile information
                            db.collection("users").document(senderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot senderSnapshot) {
                                    String senderAd = senderSnapshot.getString("ad");
                                    String senderSoyad = senderSnapshot.getString("soyad");
                                    String senderProfilePhoto = senderSnapshot.getString("profilePhoto");

                                    String id = senderSnapshot.getId();
                                    //String receiver = friendRequestDocument.getString("receiver");

                                    Student std = new Student(id, senderAd, senderSoyad, "", "", senderProfilePhoto);
                                    stdList.add(std);

                                    FriendRequestAdapter userAdapter = new FriendRequestAdapter(stdList);
                                    recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
                                    userAdapter.setOnItemClickListener(FriendRequestsFragment.this);
                                    recyclerViewUsers.setAdapter(userAdapter);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed to retrieve sender's profile information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to retrieve friend requests: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onItemClick(Student item) {
        // Create a new instance of the fragment
        ProfileShowFragment fragment = new ProfileShowFragment();

        // Create a bundle to store the arguments
        Bundle args = new Bundle();

        // Add the arguments to the bundle
        args.putString("primaryId", item.getId());
        args.putInt("count", 10);

        // Set the arguments on the fragment
        fragment.setArguments(args);

        // Replace or add the fragment using a FragmentManager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public void onAcceptButtonClick(Student item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String situation = "Accepted"; // Replace with the desired situation value

        // Create a new document in the "relationships" collection
        DocumentReference relationshipRef = db.collection("friendshiprequests").document(item.getId());

        // Create a data object to hold the relationship details
        Map<String, Object> relationshipData = new HashMap<>();
        relationshipData.put("situation", situation);

        // Set the data on the document
        relationshipRef.update(relationshipData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Friend Request Successfully Accepted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while creating the document
                        // Handle the error accordingly
                        Toast.makeText(getActivity(), "Error: Request is not Accepted", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRejectButtonClick(Student item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String situation = "Rejected"; // Replace with the desired situation value

        // Create a new document in the "relationships" collection
        DocumentReference relationshipRef = db.collection("friendshiprequests").document(item.getId());

        // Create a data object to hold the relationship details
        Map<String, Object> relationshipData = new HashMap<>();
        relationshipData.put("situation", situation);

        // Set the data on the document
        relationshipRef.set(relationshipData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Friend Request Successfully Accepted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while creating the document
                        // Handle the error accordingly
                        Toast.makeText(getActivity(), "Error: Request is not Accepted", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_searchresult, container, false);
    }
}