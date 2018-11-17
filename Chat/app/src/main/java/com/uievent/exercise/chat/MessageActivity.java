package com.uievent.exercise.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uievent.exercise.chat.Adapter.MessageAdapter;
import com.uievent.exercise.chat.Model.Chat;
import com.uievent.exercise.chat.Model.User;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_img;
    TextView username;
    Toolbar toolbar;
    EditText text_send;
    ImageButton btn_send;

    MessageAdapter messageAdapter;
    List<Chat> listChat;
    RecyclerView recycler_view;

    FirebaseUser auth;
    DatabaseReference node;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Anhxa();

        Doing();

    }

    private void Doing() {
        intent  = getIntent();
        final String fUrserid = intent.getStringExtra("Friend_userid");
        auth    = FirebaseAuth.getInstance().getCurrentUser();

        node    = FirebaseDatabase.getInstance().getReference().child("Users").child(fUrserid);
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User fUser  = dataSnapshot.getValue(User.class);
                assert fUser != null;
                username.setText(fUser.getUsername());
                if (fUser.getImageURL().equals("Default"))
                {
                    profile_img.setImageResource(R.mipmap.ic_launcher);
                }
                else Glide.with(MessageActivity.this).load(fUser.getImageURL()).into(profile_img);

                //Luu y vi sao dat o day => đặt ở đây vì khi có thay đổi ở
                loadMessage(auth.getUid(),fUrserid,fUser.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message  = text_send.getText().toString();
                if(!message.equals(""))
                {
                    sendMessage(auth.getUid(),fUrserid,message);
                }
                else Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                text_send.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String mess)
    {
        DatabaseReference nodeChat  = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("mess", mess);
        nodeChat.child("Chats").push().setValue(hashMap);
    }

    private void loadMessage (final String myId, final String userId, final String imageUrl)
    {
        listChat    = new ArrayList<>();
        node    = FirebaseDatabase.getInstance().getReference().child("Chats");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat   = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if((chat.getSender().equals(myId) && chat.getReceiver().equals(userId))
                            || (chat.getSender().equals(userId) && chat.getReceiver().equals(myId)))
                    {
                        listChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,listChat,imageUrl);
                    recycler_view.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Anhxa() {
        profile_img = findViewById(R.id.profile_img);
        username    = findViewById(R.id.username);
        toolbar     = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text_send   = findViewById(R.id.text_send);
        btn_send   = findViewById(R.id.btn_send);
        recycler_view   = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //chưa hiểu
        linearLayoutManager.setStackFromEnd(true);
        recycler_view.setLayoutManager(linearLayoutManager);
    }
}
