package org.testytu.mezunapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder> {

    private ArrayList<Student> userList;
    Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Student item);
        void onAcceptButtonClick(Student item);
        void onRejectButtonClick(Student item);
    }
    public FriendRequestAdapter(ArrayList<Student> userList) {
        this.userList = userList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_duyuru, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Student user = userList.get(position);
        holder.senderIdView.setText(user.getId());
        holder.senderNameView.setText(user.getAd()+" "+user.getSoyad());
        Picasso.get().load(user.getProfilePhotoURL()).into(holder.photoImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(user);
                }
            }
        });

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onAcceptButtonClick(user);
                }
            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRejectButtonClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView senderIdView;
        public TextView senderNameView;
        public Button acceptBtn;
        public Button rejectBtn;
        public ImageView photoImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderIdView = itemView.findViewById(R.id.senderID);
            acceptBtn = itemView.findViewById(R.id.btnAccept);
            rejectBtn = itemView.findViewById(R.id.btnReject);
            senderNameView = itemView.findViewById(R.id.senderName);
            photoImageView = itemView.findViewById(R.id.imgProfile);
        }
    }
}
