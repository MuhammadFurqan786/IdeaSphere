package com.example.stormeducation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.stormeducation.R;

public class ShowNewsActivity extends AppCompatActivity {

    TextView textView_news,textView_title;
    String news_title,news_text;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        textView_news = findViewById(R.id.news_text);
        textView_title = findViewById(R.id.news_title);
        searchView = findViewById(R.id.searchView);
        searchView.setVisibility(View.GONE);

        Intent intent = getIntent();
        news_title = intent.getStringExtra("news_title");
        news_text = intent.getStringExtra("news_text");

        textView_title.setText(news_title);
        textView_news.setText(news_text);
    }
}