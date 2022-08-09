package com.example.stormeducation.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Utilities;
import com.example.stormeducation.activities.BoardDetailActivity;
import com.example.stormeducation.models.BoardModel;
import com.example.stormeducation.models.NewsFeedModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> implements Filterable {
    ArrayList<BoardModel> list;
    ArrayList<BoardModel> listFull;
    Context context;

    public BoardAdapter(ArrayList<BoardModel> list, Context context) {
        this.list = list;
        listFull = new ArrayList<>(list);
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_board, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardModel model = list.get(position);
        holder.tv_question.setText(model.getText());

        if (list.get(position).getType().equals("Complete My Game / Lesson")){
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.new_design_blue));
        }else if (list.get(position).getType().equals("Solve My Problem")){
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.new_design_red));
        }else {
            holder.layout.setBackground(context.getResources().getDrawable(R.drawable.new_design_purple));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = list.get(position).getUsername();
                String date = list.get(position).getDate();
                String question = list.get(position).getText();
                String postId = list.get(position).getPostid();
                String uId = list.get(position).getUserId();
                String type = list.get(position).getType();
                Intent intent = new Intent(context, BoardDetailActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("date",date);
                intent.putExtra("question",question);
                intent.putExtra("postId",postId);
                intent.putExtra("uId",uId);
                intent.putExtra("intentBoard",type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_question;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.text);
            layout = itemView.findViewById(R.id.layout);

        }
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BoardModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BoardModel item : listFull) {
                    if (item.getText().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}


