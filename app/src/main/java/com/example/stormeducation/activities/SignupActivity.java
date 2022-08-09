package com.example.stormeducation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shasin.notificationbanner.Banner;

public class SignupActivity extends AppCompatActivity {

    EditText et_username,et_email,et_password;
    Button button_signUp;
    KProgressHUD hud;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);


        button_signUp = findViewById(R.id.btn_Signup);

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=  et_username.getText().toString();
                String email=  et_email.getText().toString();
                String password =  et_password.getText().toString();

                if (username.isEmpty()){
                    et_username.setError("Username is missing");
                    Banner.make(getWindow().getDecorView().getRootView(), SignupActivity.this,
                            Banner.ERROR,"Please Add Missing Fields",Banner.TOP);
                    Banner.getInstance().setDuration(5000);
                    Banner.getInstance().show();
                }else if (email.isEmpty()){
                    et_email.setError("Email is missing");
                    Banner.make(getWindow().getDecorView().getRootView(), SignupActivity.this,
                            Banner.ERROR,"Please Add Missing Fields",Banner.TOP);
                    Banner.getInstance().setDuration(5000);
                    Banner.getInstance().show();
                }else if (password.isEmpty()){
                    et_password.setError("Password is missing");

                }else
                {
                    hud = KProgressHUD.create(SignupActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();

                    auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                hud.dismiss();
                                Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                String uid = auth.getUid();
                                assert uid != null;
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.USER).child(uid);

                                reference.child("uid").setValue(uid);
                                reference.child("username").setValue(username);
                                reference.child("email").setValue(email);
                                reference.child("image").setValue("");

                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            }else{
                                hud.dismiss();
                                Toast.makeText(SignupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }

    public void gotoLogin(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}