package com.example.chat_r4;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    TextView tvVerifyEmailAddress, tvName, tvProfession, tvHourlyRate;
    Button btnLogout, btnResetPassword, btnEditProfile, btnSendEmail;
    RatingBar ratingBar;
    ImageView ivProfilePic;

    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        loadUser();

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog dialog = showMyDialog("Sending Email...");

                user.sendEmailVerification()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Profile.this, "Email sent...", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog dialog = showMyDialog("Sending Password Reset Email...");

                auth.sendPasswordResetEmail(Objects.requireNonNull(user.getEmail()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Toast.makeText(Profile.this, "Check your email", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder editProfile = new AlertDialog.Builder(Profile.this);
                editProfile.setMessage("Edit your profile");
                View view = LayoutInflater.from(Profile.this)
                        .inflate(R.layout.edit_profile_form, null, false);
                editProfile.setView(view);

                // hooks of edit profile form
                EditText etName = view.findViewById(R.id.etName);
                EditText etProfession = view.findViewById(R.id.etProfession);
                EditText etHourlyRate = view.findViewById(R.id.etHourlyRate);

                if(!tvName.getText().toString().contains("Name")) {
                    etName.setText(tvName.getText());
                    etProfession.setText(tvProfession.getText());
                    etHourlyRate.setText(tvHourlyRate.getText());
                }
                editProfile.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString().trim();
                        String prof = etProfession.getText().toString().trim();
                        String rate = etHourlyRate.getText().toString().trim();

                        if(TextUtils.isEmpty(name))
                        {
                            etName.setError("Name can't be empty");
                            return;
                        }

                        if(TextUtils.isEmpty(prof))
                        {
                            etProfession.setError("Profession can't be empty");
                            return;
                        }

                        if(TextUtils.isEmpty(rate))
                        {
                            etHourlyRate.setError("Hourly Rate can't be empty");
                            return;
                        }

                        HashMap<String, Object> userInfo = new HashMap<>();
                        userInfo.put("name", name);
                        userInfo.put("profession", prof);
                        userInfo.put("hourly_rate", rate);

                        ProgressDialog saveInfoDialog = showMyDialog("Saving your information...");

                        reference.child("Users")
                                .child(user.getUid())
                                .setValue(userInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        saveInfoDialog.dismiss();
                                        Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        saveInfoDialog.dismiss();
                                        Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                editProfile.show();

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });
    }

    private void loadUser() {
        reference.child("Users")
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            String name = snapshot.child("name").getValue().toString();
                            String prof = snapshot.child("profession").getValue().toString();
                            String rate = snapshot.child("hourly_rate").getValue().toString();

                            tvName.setText(name);
                            tvProfession.setText(prof);
                            tvHourlyRate.setText(rate);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private ProgressDialog showMyDialog(String s) {
        ProgressDialog dialog = new ProgressDialog(Profile.this);
        dialog.setMessage(s);
        dialog.show();

        return dialog;
    }

    private void init()
    {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvVerifyEmailAddress = findViewById(R.id.tvVerifyYourAccount);
                 tvName = findViewById(R.id.tvName);
        tvProfession = findViewById(R.id.tvProfession);
                tvHourlyRate = findViewById(R.id.tvHourlyRate);
        btnLogout = findViewById(R.id.btnLogout);
                btnResetPassword = findViewById(R.id.btnResetPassword);
        btnEditProfile = findViewById(R.id.btnEditProfile) ;
                btnSendEmail = findViewById(R.id.btnSendEmail) ;
        ratingBar = findViewById(R.id.ratingBar) ;
        ivProfilePic = findViewById(R.id.ivProfilePic);

        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tvVerifyEmailAddress.setVisibility(View.GONE);
        btnSendEmail.setVisibility(View.GONE);

        if(!user.isEmailVerified())
        {
            tvVerifyEmailAddress.setVisibility(View.VISIBLE);
            btnSendEmail.setVisibility(View.VISIBLE);
        }
    }
}