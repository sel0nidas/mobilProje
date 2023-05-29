package org.testytu.mezunapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment implements MyAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchResultFragment() {
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

    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
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

        String entranceYear = getArguments().getString("entranceYear");
        String graduationYear = getArguments().getString("graduationYear");
        //String graduationYear = "2024";

        DatabaseReference mDatabase;
        ActivityResultLauncher<String> pickImageLauncher;

        RecyclerView recyclerViewUsers = view.findViewById(R.id.userRecycler);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query usersQuery = db.collection("users");
        CollectionReference usersRef = db.collection("users");

        // Start with the base query
        Query query = usersQuery;

        // Check if graduationYear is provided
        if (!TextUtils.isEmpty(graduationYear)) {
            query = query.whereEqualTo("mezunYil", graduationYear);
        }
        // Check if entranceYear is provided
        if (!TextUtils.isEmpty(entranceYear)) {
            query = query.whereEqualTo("girisYil", entranceYear);
        }
        /*
        // Check if city is provided
        if (!TextUtils.isEmpty(city)) {
            query = query.whereEqualTo("city", city);
        }
        */
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                }
                MyAdapter userAdapter = new MyAdapter(stdList);
                recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
                userAdapter.setOnItemClickListener(SearchResultFragment.this);

                recyclerViewUsers.setAdapter(userAdapter);
            }
        });
    }

    //@Override
    public void onItemClick(Student item) {
        //Toast.makeText(this, item.getAd(), Toast.LENGTH_SHORT).show();
        /*
        Intent intent = new Intent(getActivity(), ProfileOnlyShowActivity.class);
        intent.putExtra("primaryId", item.getId());
        startActivity(intent);
        */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_searchresult, container, false);
    }
}