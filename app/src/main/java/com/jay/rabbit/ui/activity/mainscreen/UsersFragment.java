package com.jay.rabbit.ui.activity.mainscreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jay.rabbit.R;
import com.jay.rabbit.adapter.UserAdapter;
import com.jay.rabbit.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {


    private static final String TAG = "LOG_TAG";
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> users;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_users);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        users = new ArrayList<>();

        readUsers();

        return view;
    }

    private void readUsers() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = snapshot.getValue(User.class);

                    if (firebaseUser != null && user != null &&
                            !user.getId().equals(firebaseUser.getUid())) {

                        users.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), users);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}