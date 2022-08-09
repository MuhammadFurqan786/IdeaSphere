package com.example.stormeducation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.example.stormeducation.fragments.NewsFeedFragment;
import com.example.stormeducation.models.BoardModel;
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

public class AskquestionActivity extends AppCompatActivity {

    EditText et_question;
    ImageView btn_send;
    KProgressHUD hud;
    String date,username;
    Intent intent;
    TextView textView;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askquestion);

        getUserdetails();
        intent = getIntent();
        text = intent.getStringExtra("intent");

        et_question = findViewById(R.id.et_question);
        btn_send = findViewById(R.id.btn_send_question);
        textView = findViewById(R.id.text_question);

        textView.setText(text);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = et_question.getText().toString();
                if (question.isEmpty()) {
                    et_question.setError("Please Enter a question");
                } else {
                    hud = KProgressHUD.create(AskquestionActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    postQuestion(question);

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
                           Toast.makeText(AskquestionActivity.this, ""+username, Toast.LENGTH_SHORT).show();
                       }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @SuppressLint("SimpleDateFormat")
    private void postQuestion(String question) {

        date = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
        String uid = FirebaseAuth.getInstance().getUid();


        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(Constants.QUESTIONS);
        // Generate Unique key
        String key = reference.push().getKey();
        assert key !=null;
        BoardModel model = new BoardModel(key,question,uid,username,date,text);
        reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hud.dismiss();
                if (task.isSuccessful()){
                    Banner.make(getWindow().getDecorView().getRootView(), AskquestionActivity.this,
                            Banner.SUCCESS,"Your Question Posted Successfully",Banner.TOP);
                    Banner.getInstance().setDuration(5000);
                    Banner.getInstance().show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("fromQuestion","1");
                    startActivity(intent);
                    finish();
                }else{
                    Banner.make(getWindow().getDecorView().getRootView(), AskquestionActivity.this,
                            Banner.ERROR,"Failed to send your post",Banner.TOP);
                    Banner.getInstance().setDuration(5000);
                    Banner.getInstance().show();
                }
            }
        });
    }
}