package com.example.stormeducation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.example.stormeducation.models.BoardModel;
import com.example.stormeducation.models.NewsFeedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shasin.notificationbanner.Banner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddNewsActivity extends AppCompatActivity {
    EditText et_news,et_title;
    TextView btn_send;
    KProgressHUD hud;
    String date,username,image;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        getUserdetails();

        et_news = findViewById(R.id.et_news);
        et_title = findViewById(R.id.et_title);
        btn_send = findViewById(R.id.share);
        searchView = findViewById(R.id.searchView);
        searchView.setVisibility(View.GONE);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String news = et_news.getText().toString();
                String title = et_title.getText().toString();
                if (news.isEmpty()) {
                    et_news.setError("Please Enter Something");
                }else if (title.isEmpty()){
                    et_title.setError("Please Enter title here...");
                }
                else {
                    hud = KProgressHUD.create(AddNewsActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    postQuestion(news,title);

                }
            }
        });
    }

    private void getUserdetails() {
        FirebaseDatabase.getInstance().getReference().child(Constants.USER)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            username = snapshot.child("username").getValue().toString();
                            image = snapshot.child("image").getValue().toString();
                            Toast.makeText(AddNewsActivity.this, ""+username, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    private void postQuestion(String news, String question) {

        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        String uid = FirebaseAuth.getInstance().getUid();


        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.NEWS);
        // Generate Unique key
        String key = reference.push().getKey();
        assert key !=null;
        NewsFeedModel model = new NewsFeedModel(image,question,news);
        reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hud.dismiss();
                if (task.isSuccessful()){
                    Banner.make(getWindow().getDecorView().getRootView(), AddNewsActivity.this,
                            Banner.SUCCESS,"Your News Posted Successfully",Banner.TOP);
                    Banner.getInstance().setDuration(5000);
                    Banner.getInstance().show();
                    Intent intent = new Intent(AddNewsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Banner.make(getWindow().getDecorView().getRootView(), AddNewsActivity.this,
                            Banner.ERROR,"Failed to send your post",Banner.TOP);
                    Banner.getInstance().setDuration(5000);
                    Banner.getInstance().show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

}