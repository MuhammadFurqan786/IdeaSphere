package com.example.stormeducation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stormeducation.R;
import com.example.stormeducation.Utils.Constants;
import com.example.stormeducation.Utils.Utilities;
import com.example.stormeducation.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.shasin.notificationbanner.Banner;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button button_login;
    EditText et_email,et_password;
    KProgressHUD hud;
    FirebaseAuth auth;
    String isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isLoggedIn =  Utilities.getString(getApplicationContext(),"login");
        if (isLoggedIn.equals("yes")){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);



        button_login = findViewById(R.id.btn_Login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String email = et_email.getText().toString();
               String pass = et_password.getText().toString();

               if (email.isEmpty()){
                    et_email.setError("Email is missing");
                   Banner.make(getWindow().getDecorView().getRootView(), LoginActivity.this,Banner.ERROR,"Please Add Missing Fields",Banner.TOP);
                   Banner.getInstance().setDuration(5000);
                   Banner.getInstance().show();
                }else if (pass.isEmpty()){
                    et_password.setError("Password is missing");
                   Banner.make(getWindow().getDecorView().getRootView(), LoginActivity.this,Banner.ERROR,"Please Add Missing Fields",Banner.TOP);
                   Banner.getInstance().setDuration(5000);
                   Banner.getInstance().show();
                }else
                {
                    hud = KProgressHUD.create(LoginActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setLabel("Please wait")
                            .setCancellable(true)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();
                    auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                hud.dismiss();

                                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                FirebaseDatabase.getInstance().getReference(Constants.USER)
                                        .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            UserModel userModel = snapshot.getValue(UserModel.class);
                                            if (userModel != null) {
                                                //saving in app shared pref

                                                Utilities.saveString(getApplicationContext(),"login","yes");
                                                Toast.makeText(LoginActivity.this, "Welcome"+userModel.username, Toast.LENGTH_SHORT).show();

                                                Utilities.saveString(getApplicationContext(),"username",userModel.username);
                                                Utilities.saveString(getApplicationContext(),"userimage",userModel.image);
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Banner.make(getWindow().getDecorView().getRootView(),
                                                        LoginActivity.this,Banner.ERROR,"Error",Banner.TOP);
                                                Banner.getInstance().setDuration(5000);
                                                Banner.getInstance().show();                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Banner.make(getWindow().getDecorView().getRootView(), LoginActivity.this,Banner.ERROR,"Error Message :"+error.getMessage(),Banner.TOP);
                                        Banner.getInstance().setDuration(5000);
                                        Banner.getInstance().show();
                                    }
                                });
                            } else {
                                hud.dismiss();
                                Banner.make(getWindow().getDecorView().getRootView(),
                                        LoginActivity.this,Banner.ERROR,"Failed",Banner.TOP);
                                Banner.getInstance().setDuration(5000);
                                Banner.getInstance().show();
                            }
                        }
                    });

                }
            }
        });
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }


}