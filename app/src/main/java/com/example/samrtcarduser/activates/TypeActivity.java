package com.example.samrtcarduser.activates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.samrtcarduser.R;
import com.example.samrtcarduser.adapter.HomeAdapter;
import com.example.samrtcarduser.helper.HomeHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TypeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<HomeHelper> list;


    private TextView tvNotFound;

    private ProgressBar progressBar;

    private DatabaseReference reference;

    private Intent intent;
    private String nameParent;

    private SharedPreferences preferences;
    private String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        getSupportActionBar().setTitle("Type");

        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);

        initiateView();
        fullRecyclerView();
    }

    private void initiateView() {

        intent = getIntent();
        nameParent = intent.getStringExtra("NAME_CARD");
        recyclerView = findViewById(R.id.recycler_all_type);
        recyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();

        tvNotFound = findViewById(R.id.tv_not_found);



        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + nameParent);
    }

    private void fullRecyclerView() {

        final Query userQuery = reference;

        userQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name = snapshot.getKey();
                            System.out.println("name " + name);
                            if (!name.equals("picCard")) {
                                String imageView = snapshot.child("picCard").getValue().toString();
                                list.add(new HomeHelper(name, imageView));
                            }
                        }
                        adapter = new HomeAdapter(TypeActivity.this, list);
                        recyclerView.setAdapter(adapter);
                        if (list.isEmpty()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            tvNotFound.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            tvNotFound.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}