package com.example.chat_r4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BlogAdapter extends FirebaseRecyclerAdapter<Blog, BlogAdapter.BlogViewHolder> {

    public BlogAdapter(@NonNull FirebaseRecyclerOptions<Blog> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BlogViewHolder holder, int i, @NonNull Blog blog) {
        holder.tvTitle.setText(blog.getTitle());
        holder.tvDesc.setText(blog.getDescription());
        holder.tvTimeStamp.setText(blog.getTimestamp());
        holder.tvLikes.setText(blog.getLikes()+"");

    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_blog_design, parent, false);
        return new BlogViewHolder(v);
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle, tvDesc, tvTimeStamp, tvLikes;
        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.tvDescription);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLikes = itemView.findViewById(R.id.tvLikes);


        }
    }
}
