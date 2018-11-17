package com.uievent.exercise.chat.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.uievent.exercise.chat.Model.Chat;
import com.uievent.exercise.chat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Chat> listChat;
    private String imageUrl;

    private static final int TIN_NHAN_TRAI = 0;
    private static final int TIN_NHAN_PHAI = 1;

    public MessageAdapter(Context mContext, List<Chat> listChat, String imageUrl) {
        this.mContext = mContext;
        this.listChat = listChat;
        this.imageUrl = imageUrl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_img;
        TextView    show_message;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img  = itemView.findViewById(R.id.profile_img);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }

//    biến i là texyType

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==TIN_NHAN_TRAI)
        {
            View view;
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_view_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            View view;
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_view_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat chat   = listChat.get(i);
        viewHolder.show_message.setText(chat.getMess());
        if(imageUrl.equals("Default"))
        {
            viewHolder.profile_img.setImageResource(R.mipmap.ic_launcher);
        }
        else
            Glide.with(mContext).load(imageUrl).into(viewHolder.profile_img);

    }

    @Override
    public int getItemCount() {
        return listChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser myuser = FirebaseAuth.getInstance().getCurrentUser();
        assert myuser != null;
        if (listChat.get(position).getSender().equals(myuser.getUid()))
        {
            return TIN_NHAN_PHAI;
        }
        else return TIN_NHAN_TRAI;
    }
}
