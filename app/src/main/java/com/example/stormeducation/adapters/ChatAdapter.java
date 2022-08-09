package com.example.stormeducation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stormeducation.R;
import com.example.stormeducation.models.BoardModel;
import com.example.stormeducation.models.ChatModel;

import java.util.ArrayList;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    ArrayList<ChatModel> list;
    Context context;

    public ChatAdapter(ArrayList<ChatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_chats, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatModel model = list.get(position);
        holder.tv_name.setText(model.getName());
        holder.tv_message.setText(model.getMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.chat_name);
            tv_message = itemView.findViewById(R.id.chat_message);
        }
    }
}


