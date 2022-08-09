package com.example.stormeducation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stormeducation.R;
import com.example.stormeducation.models.ChatModel;
import com.example.stormeducation.models.CommentsModel;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<CommentsModel> list;
    Context context;

    public CommentAdapter(ArrayList<CommentsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        CommentsModel model = list.get(position);
        holder.tv_name.setText(model.getUsername());
        holder.tv_comment.setText(model.getComment());
        holder.time.setText(model.getDate());
        holder.time.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_comment,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_cmnt_name);
            time = itemView.findViewById(R.id.tv_cmnt_time);
            tv_comment = itemView.findViewById(R.id.tv_cmnt);

        }
    }
}


