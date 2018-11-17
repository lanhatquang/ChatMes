package com.uievent.exercise.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout email,pass,username;
    Button btn_register;
    Toolbar toolbar;
    DatabaseReference mData;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mData   = FirebaseDatabase.getInstance().getReference();
        auth    = FirebaseAuth.getInstance();
        Anhxa();
        Doing();

    }

    private void Doing() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ChuanHoaPass() | !ChuanHoaEmail() | !ChuanHoaUser()) return;
                String textEmail    = email.getEditText().getText().toString().trim();
                String textPass     = pass.getEditText().getText().toString().trim();
                String textUsername = username.getEditText().getText().toString().trim();
                DangKy(textEmail,textPass,textUsername);
            }
        });
    }

    private void DangKy (String email, String pass, final String username)
    {
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String userid   = user.getUid();
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("email", user.getEmail());
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "Default");
                            mData.child("Users").child(userid).setValue(hashMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null)
                                    {
                                        Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                            Toast.makeText(RegisterActivity.this, "Register Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean ChuanHoaEmail()
    {
        String emailInput = email.getEditText().getText().toString().trim();
        if(emailInput.isEmpty())
        {
            email.setError("Field can't be empty");
            return false;
        }
        else
        {
            email.setError(null);
            return true;
        }
    }

    private boolean ChuanHoaPass()
    {
        String passInput = pass.getEditText().getText().toString().trim();
        if(passInput.isEmpty())
        {
            pass.setError("Field can't be empty");
            return false;
        }
        else
        {
            pass.setError(null);
            return true;
        }
    }

    private boolean ChuanHoaUser()
    {
        String userInput = username.getEditText().getText().toString().trim();
        if(userInput.isEmpty())
        {
            username.setError("Field can't be empty");
            return false;
        }
        else if(userInput.length() > 15)
        {
            username.setError("Username too long");
            return false;
        }
        else
        {
            username.setError(null);
            return true;
        }
    }

    private void Anhxa() {
        email           = findViewById(R.id.email);
        pass            = findViewById(R.id.pass);
        username        = findViewById(R.id.username);
        btn_register    = findViewById(R.id.btn_register);
        toolbar         = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
