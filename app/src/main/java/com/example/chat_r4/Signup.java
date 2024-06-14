package com.example.chat_r4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    EditText etEmail, etPassword, etCPassword;
    Button btnSignup;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString();
                String cpass = etCPassword.getText().toString();

                if(email.isEmpty())
                {
                    etEmail.setError("Email cant be empty");
                    return;
                }

                if(pass.isEmpty())
                {
                    etPassword.setError("Email cant be empty");
                    return;
                }

                if(cpass.isEmpty())
                {
                    etCPassword.setError("Email cant be empty");
                    return;
                }

                if(!pass.equals(cpass))
                {
                    etCPassword.setError("pass didn't match");
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(Signup.this);
                progressDialog.setMessage("Registration in progress.....");
                progressDialog.show();


                FirebaseAuth
                        .getInstance()
                        .createUserWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                startActivity(new Intent(Signup.this, Profile.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

    private void openLoginActivity() {
        startActivity(new Intent(Signup.this, MainActivity.class));
        finish();
    }
    private void init()
    {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        tvLogin = findViewById(R.id.tvLogin);
        btnSignup = findViewById(R.id.btnSignup);

    }


}