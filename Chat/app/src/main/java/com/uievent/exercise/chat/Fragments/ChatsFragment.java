package com.uievent.exercise.chat.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.uievent.exercise.chat.Model.Chat;
import com.uievent.exercise.chat.Model.User;
import com.uievent.exercise.chat.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment{
    public static int CHECK =0;
    private RecyclerView recyclerView;
    private List<User> listUser;
    private UserAdapter userAdapter;
    private List<String> listString;

    DatabaseReference node;
    FirebaseUser myUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view   = inflater.inflate(R.layout.fragment_chats, container, false);
        Anhxa(view);
        myUser  = FirebaseAuth.getInstance().getCurrentUser();

        listString = new ArrayList<>();

        node    = FirebaseDatabase.getInstance().getReference().child("Chats");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listString.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat   = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getSender().equals(myUser.getUid()))
                    {
                        listString.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(myUser.getUid()))
                    {
                        listString.add(chat.getSender());
                    }
                }

                LoadList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
    private void  LoadList() {
        listUser    = new ArrayList<>();

        node    = FirebaseDatabase.getInstance().getReference().child("Users");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    CHECK = 0;
                    User user   = snapshot.getValue(User.class);
                    // duyệt tất cả các node con của User và tìm xem User nào đang nhắn tin với myUser.
                    for (String id : listString)
                    {
                        assert user != null;
                        if (user.getId().equals(id))
                        {
                            // tại đây xảy ra 2 trường hợp, 1 là trong listUser đã tồn tại, 2 là listUser rỗng.
                            if (listUser.size() != 0)
                            {
                                for(User tmp : listUser)
                                {
                                    if(user.getId().equals(tmp.getId())) {CHECK = 1;break;}
                                }
                                if(CHECK==0) listUser.add(user);
                            }
                            else listUser.add(user);
                        }
                    }
                }
                //TO DO
                userAdapter = new UserAdapter(getContext(),listUser);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void Anhxa(View view) {
        recyclerView   = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}

