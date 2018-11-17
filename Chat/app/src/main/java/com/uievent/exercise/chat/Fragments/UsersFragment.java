package com.uievent.exercise.chat.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.uievent.exercise.chat.Adapter.UserAdapter;
import com.uievent.exercise.chat.Model.User;
import com.uievent.exercise.chat.R;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    RecyclerView recycler_view;


    UserAdapter userAdapter;
    List<User> listUser;

    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recycler_view   = view.findViewById(R.id.recycler_view);
        // Nếu các Item có cùng chiều cao và độ rộng thì tối ưu hiệu năng để khi cuộn danh sách được mượt mà hơn
        recycler_view.setHasFixedSize(true);
        // Set kiểu hiển thị của RecyclerView, có 2 cách hiển thị vertical và horizontal trong LinearLayout
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));


        readUsers();

        return view;
    }

    private void readUsers() {
        listUser = new ArrayList<>();

        DatabaseReference node    = FirebaseDatabase.getInstance().getReference().child("Users");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                // Cấp quyền truy cập đến từng node con của node Users để đọc
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    // Tạo biến User để lưu khi đọc
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid()))
                    {
                        listUser.add(user);
                    }

                }
                userAdapter = new UserAdapter(getContext(),listUser);
                recycler_view.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

