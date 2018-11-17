package com.uievent.exercise.chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uievent.exercise.chat.MessageActivity;
import com.uievent.exercise.chat.Model.User;
import com.uievent.exercise.chat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> listUsers;


    public UserAdapter(Context mContext, List<User> listUsers) {
        this.mContext = mContext;
        this.listUsers = listUsers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView username;
        CircleImageView profile_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username    = itemView.findViewById(R.id.username);
            profile_img = itemView.findViewById(R.id.profile_img);

        }
    }

    // Custom RecyclerView bằng ViewHolder

    @NonNull
    @Override
    //khai bao view hien thi len moi dong
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_layout_user, viewGroup,false);
        return new ViewHolder(view);
    }

    //gan gia tri cho view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = listUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if(user.getImageURL().equals("Default"))
        {
            viewHolder.profile_img.setImageResource(R.mipmap.ic_launcher);
        }
        else Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("Friend_userid",user.getId());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listUsers.size();
    }
}
