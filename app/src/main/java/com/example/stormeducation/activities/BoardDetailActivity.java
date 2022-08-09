package com.example.stormeducation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.example.stormeducation.Utils.Utilities;
import com.example.stormeducation.adapters.CommentAdapter;
import com.example.stormeducation.models.BoardModel;
import com.example.stormeducation.models.CommentsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shasin.notificationbanner.Banner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoardDetailActivity extends AppCompatActivity {

    TextView tv_username, tv_date, tv_question;
    String date, name, question, postId, uId,type;
    EditText et_comment;
    ImageView button_comment;
    KProgressHUD hud;
    RecyclerView rv_comments;
    ArrayList<CommentsModel> arrayList;
    LinearLayout details_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);

        tv_date = findViewById(R.id.tv_date);
        tv_question = findViewById(R.id.tv_question);
        tv_username = findViewById(R.id.tv_username);
        et_comment = findViewById(R.id.et_comment);
        button_comment = findViewById(R.id.send_comment);
        rv_comments = findViewById(R.id.rv_comments);
        details_layout = findViewById(R.id.details_layout);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_comments.setLayoutManager(manager);
        rv_comments.setHasFixedSize(true);

        arrayList = new ArrayList<>();


        Intent intent = getIntent();

        name  = Utilities.getString(getApplicationContext(),"username");
        date = intent.getStringExtra("date");
        question = intent.getStringExtra("question");
        postId = intent.getStringExtra("postId");
        uId = intent.getStringExtra("uId");
        type = intent.getStringExtra("intentBoard");

        if (type.equals("Complete My Game / Lesson")){
            details_layout.setBackground(getResources().getDrawable(R.drawable.new_design_blue));
        }else if (type.equals("Solve My Problem")){
            details_layout.setBackground(getResources().getDrawable(R.drawable.new_design_red));
        }else {
            details_layout.setBackground(getResources().getDrawable(R.drawable.new_design_purple));
        }


        tv_date.setText(date);
        tv_username.setText(name);
        tv_question.setText(question);

        getComments();

        button_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hud = KProgressHUD.create(BoardDetailActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Please wait")
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();

                String comment = et_comment.getText().toString();
                String date = new SimpleDateFormat("DD-MM-YYYY HH:mm:ss").format(new Date());
                if (comment.isEmpty()) {
                    Toast.makeText(BoardDetailActivity.this, "Please Write Something", Toast.LENGTH_SHORT).show();
                }

                CommentsModel model = new CommentsModel(postId, comment, name, date);

                FirebaseDatabase database =FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference(Constants.COMMENTS).child(postId);
                // Generate Unique key
                String key = reference.push().getKey();
                assert key !=null;

                FirebaseDatabase.getInstance().getReference(Constants.COMMENTS)
                        .child(postId).child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BoardDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            et_comment.setText("");
                            getComments();
                        } else {
                            Banner.make(getWindow().getDecorView().getRootView(),
                                    BoardDetailActivity.this,
                                    Banner.ERROR, "Failed", Banner.TOP);
                            Banner.getInstance().setDuration(5000);
                            Banner.getInstance().show();
                        }
                    }
                });
            }
        });

    }

    private void getComments() {
        hud = KProgressHUD.create(BoardDetailActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        FirebaseDatabase.getInstance().getReference(Constants.COMMENTS).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hud.dismiss();
                arrayList.clear();
                if (snapshot.exists()){

                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CommentsModel temp = snapshot1.getValue(CommentsModel.class);
                        arrayList.add(temp);
                    }
                        feedData(arrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hud.dismiss();
                Banner.make(getWindow().getDecorView().getRootView(),
                        BoardDetailActivity.this,
                        Banner.ERROR, "Error while getting data", Banner.TOP);
                Banner.getInstance().setDuration(5000);
                Banner.getInstance().show();

            }
        });
    }

    private void feedData(ArrayList<CommentsModel> arrayList) {
        CommentAdapter adapter = new CommentAdapter(arrayList,this);
        rv_comments.setAdapter(adapter);
    }
}