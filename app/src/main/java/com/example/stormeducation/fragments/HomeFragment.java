package com.example.stormeducation.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.stormeducation.R;
import com.example.stormeducation.activities.AskquestionActivity;


public class HomeFragment extends Fragment {

    ImageView blue,purple,yellow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        blue = v.findViewById(R.id.imageView);
        yellow =v. findViewById(R.id.imageView2);
        purple =v.findViewById(R.id.imageView_purple);
        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.sound);

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(1*1000);


                            Intent intent = new Intent(getContext(), AskquestionActivity.class);
                            intent.putExtra("intent","Complete My Game / Lesson");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // start thread
                background.start();

            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(1*1000);


                            Intent intent = new Intent(getContext(), AskquestionActivity.class);
                            intent.putExtra("intent","Solve My Problem");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // start thread
                background.start();

            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Thread background = new Thread() {
                    public void run() {
                        try {
                            // Thread will sleep for 5 seconds
                            sleep(1*1000);


                            Intent intent = new Intent(getContext(), AskquestionActivity.class);
                            intent.putExtra("intent","Break My Negative Thought");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // start thread
                background.start();
            }

        });

        return v;
    }

}