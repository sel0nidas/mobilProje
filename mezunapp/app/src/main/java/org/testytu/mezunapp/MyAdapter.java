package org.testytu.mezunapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Student> userList;
    Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Student item);
    }
    public MyAdapter(ArrayList<Student> userList) {
        this.userList = userList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Student user = userList.get(position);
        holder.nameTextView.setText(user.getAd());
        holder.timeTextView.setText(user.getEgitimAralik());
        Picasso.get().load(user.getProfilePhotoURL()).into(holder.photoImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView;
        public TextView timeTextView;
        public ImageView photoImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.username);
            timeTextView = itemView.findViewById(R.id.timeRange);
            photoImageView = itemView.findViewById(R.id.ppUser);
        }
    }
}
