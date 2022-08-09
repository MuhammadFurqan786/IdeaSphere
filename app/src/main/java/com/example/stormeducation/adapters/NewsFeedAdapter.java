package com.example.stormeducation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stormeducation.R;
import com.example.stormeducation.activities.ShowNewsActivity;
import com.example.stormeducation.models.BoardModel;
import com.example.stormeducation.models.NewsFeedModel;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    ArrayList<BoardModel> list;
    Context context;

    public NewsFeedAdapter(ArrayList<BoardModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_newsfeed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardModel model = list.get(position);

        if (list.get(position).getType().equals("Complete My Game / Lesson")){
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.new_design_blue));
        }else if (list.get(position).getType().equals("Solve My Problem")){
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.new_design_red));
        }else {
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.new_design_purple));
        }

        holder.tv_name.setText(model.getUsername());
        holder.tv_question.setText(model.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.action_newsFragment_to_boardFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView tv_name,tv_question;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            tv_name = itemView.findViewById(R.id.profile_name);
            tv_question = itemView.findViewById(R.id.profile_question);
            layout = itemView.findViewById(R.id.layout_design);
        }
    }
}
