package com.uievent.exercise.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uievent.exercise.chat.Fragments.ChatsFragment;
import com.uievent.exercise.chat.Fragments.UsersFragment;
import com.uievent.exercise.chat.Model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_img;
    TextView username;
    TabLayout tab_layout;
    ViewPager view_pager;
    Toolbar toolbar;

    FirebaseUser user;
    DatabaseReference mData;
    DatabaseReference nodeUser;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData   = FirebaseDatabase.getInstance().getReference();
        Anhxa();
        ViewPagerAdapter viewPagerAdapter   = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");
        viewPagerAdapter.addFragment(new UsersFragment(),"Users");
        view_pager.setAdapter(viewPagerAdapter);
        tab_layout.setupWithViewPager(view_pager);

        Doing();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String>   titleOfFrags;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments      = new ArrayList<>();
            this.titleOfFrags    = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return titleOfFrags.size();
        }

        public void addFragment(Fragment fragment, String titleOfFrag)
        {
            fragments.add(fragment);
            titleOfFrags.add(titleOfFrag);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleOfFrags.get(position);
        }
    }

    private void Doing() {
        user    = FirebaseAuth.getInstance().getCurrentUser();
        //Khai bao node user
        nodeUser    = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        nodeUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user   = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("Default")) profile_img.setImageResource(R.mipmap.ic_launcher);
                else Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_img);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;
        }
        return false;
    }

    private void Anhxa() {
        profile_img = findViewById(R.id.profile_img);
        username    = findViewById(R.id.username);
        tab_layout  = findViewById(R.id.tab_layout);
        view_pager  = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }
}
