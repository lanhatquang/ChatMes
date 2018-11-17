package com.uievent.exercise.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button login,register;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser auth   = FirebaseAuth.getInstance().getCurrentUser();
        if(auth!=null) {startActivity(new Intent(StartActivity.this, MainActivity.class)); finish();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Anhxa();
        Doing();

    }

    private void Doing() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });
    }

    private void Anhxa() {
        login    = findViewById(R.id.login);
        register = findViewById(R.id.register);
    }
}
