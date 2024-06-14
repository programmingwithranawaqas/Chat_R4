package com.example.chat_r4;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class BlogActivity extends AppCompatActivity {

    FloatingActionButton fabAddBlog;
    RecyclerView rvBlogs;

    BlogAdapter adapter;

    DatabaseReference reference;
    FirebaseAuth auth;

    String uID;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
       // username = getSharedPreferences("USER", MODE_PRIVATE).getString("username","");

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blog);

        init();
        



        fabAddBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                addNewBlog();
            }
        });

    }

    public void addNewBlog() {
        AlertDialog.Builder addBlogD =
                new AlertDialog.Builder(BlogActivity.this);
        addBlogD.setTitle("New Blog");
        View v = LayoutInflater.from(BlogActivity.this)
                .inflate(R.layout.new_blog_form, null, false);
        addBlogD.setView(v);
        EditText etTitle = v.findViewById(R.id.etTitle);
        EditText etDesc = v.findViewById(R.id.etDescription);

        addBlogD.setPositiveButton("Save Blog", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = etTitle.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                if(TextUtils.isEmpty(title))
                {
                    etTitle.setError("Fill the title");
                    return;
                }

                if(TextUtils.isEmpty(desc))
                {
                    etDesc.setError("Fill the description");
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(BlogActivity.this);
                progressDialog.setMessage("Saving Blog...");
                progressDialog.show();


                HashMap<String, Object> blog = new HashMap<>();
                blog.put("title", title);
                blog.put("description", desc);
                blog.put("likes", 0);
                blog.put("timestamp", new Date().toString());

                reference.child("Blogs")
                        .child(uID)
                        .push()
                        .setValue(blog)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(BlogActivity.this, "Blog added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(BlogActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        addBlogD.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        addBlogD.show();

    }

    private void init()
    {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        auth = FirebaseAuth.getInstance();
        uID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        fabAddBlog = findViewById(R.id.fabAddBlog);
        rvBlogs = findViewById(R.id.rvBlogs);
        reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Blogs")
                .child(uID);

        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(query, Blog.class)
                        .build();


        adapter = new BlogAdapter(options);

        rvBlogs.setHasFixedSize(true);
        rvBlogs.setLayoutManager(new LinearLayoutManager(this));
        rvBlogs.setAdapter(adapter);
    }
}