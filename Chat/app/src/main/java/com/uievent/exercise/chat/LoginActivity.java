package com.uievent.exercise.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email,pass;
    Button btn_login;
    Toolbar toolbar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        Anhxa();
        Doing();

    }

    private void Doing() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ChuanHoaEmail() | !ChuanHoaPass()) return;
                String textEmail    = email.getEditText().getText().toString().trim();
                String textPass     = pass.getEditText().getText().toString().trim();
                DangNhap(textEmail,textPass);
            }
        });
    }

    private void DangNhap(String email, String pass)
    {
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent   = new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
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

    private void Anhxa() {
        email       = findViewById(R.id.email);
        pass        = findViewById(R.id.pass);
        btn_login   = findViewById(R.id.btn_login);
        toolbar         = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
