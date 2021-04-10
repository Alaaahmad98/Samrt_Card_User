package com.example.samrtcarduser.activates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samrtcarduser.R;
import com.example.samrtcarduser.adapter.NumberCardAdapter;
import com.example.samrtcarduser.helper.NumberCardHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardNumberActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NumberCardAdapter adapter;
    private List<NumberCardHelper> list;

    private TextView tvNotFound;
    private ProgressBar progressBar;

    private DatabaseReference reference;

    private Intent intent;
    private String child;

    private SharedPreferences preferences;
    private String root, parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_number);

        getSupportActionBar().setTitle("Number Card");

        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);
        parent = preferences.getString("PARENT", null);

        initiateView();
        fullRecyclerView();

        adapter.setOnItemClickListener(new NumberCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CardNumberActivity.this, PayCardActivity.class);
                intent.putExtra("NAME_CARD", child);
                startActivity(intent);
            }
        });
    }

    private void initiateView() {

        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");
        recyclerView = findViewById(R.id.recycler_all_number_Card);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new NumberCardAdapter(CardNumberActivity.this, list);

        tvNotFound = findViewById(R.id.tv_not_found);
        progressBar = findViewById(R.id.progress_bar);


        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child);
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
                            if (name.equals("CardNumber")) {

                                reference.child(name).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            String numberCard = child.getValue().toString();
                                            list.add(new NumberCardHelper(numberCard));
                                        }
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
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
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
