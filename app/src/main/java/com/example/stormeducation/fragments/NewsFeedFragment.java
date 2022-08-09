package com.example.stormeducation.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.example.stormeducation.activities.AddNewsActivity;
import com.example.stormeducation.adapters.BoardAdapter;
import com.example.stormeducation.adapters.NewsFeedAdapter;
import com.example.stormeducation.models.BoardModel;
import com.example.stormeducation.models.NewsFeedModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shasin.notificationbanner.Banner;

import java.util.ArrayList;
import java.util.Objects;


public class NewsFeedFragment extends Fragment {

    RecyclerView rv_news;
    ArrayList<BoardModel> arrayList;
    TextView title_text;
    KProgressHUD hud;
    SearchView searchView;
    NewsFeedAdapter adapter;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        rv_news = v.findViewById(R.id.rv_newsFeed);
        title_text = v.findViewById(R.id.title_text);

        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddNewsActivity.class));
                requireActivity().finish();
            }
        });

        title_text.setText("News Feed");

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Fetching Data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        rv_news.setLayoutManager(manager);
        arrayList = new ArrayList<BoardModel>();

        feedData();

        searchView = v.findViewById(R.id.searchView);
        searchView.setVisibility(View.GONE);

        return v;

    }

    private void feedData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.QUESTIONS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hud.dismiss();
                if (snapshot.exists()) {
                    for (DataSnapshot question : snapshot.getChildren()) {
                        BoardModel temp = question.getValue(BoardModel.class);
                        String id = question.getKey();
                        arrayList.add(temp);
                    }
                    setdata(arrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hud.dismiss();
                Banner.make(getActivity().getWindow().getDecorView().getRootView(), getContext(), Banner.ERROR, "Error " + error, Banner.TOP);
                Banner.getInstance().setDuration(5000);
                Banner.getInstance().show();
            }
        });

    }
    private void setdata(ArrayList<BoardModel> arrayList) {
        adapter = new NewsFeedAdapter(arrayList, getContext());
        rv_news.setAdapter(adapter);
    }
}