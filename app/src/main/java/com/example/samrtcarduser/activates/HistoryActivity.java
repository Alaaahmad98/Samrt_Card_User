package com.example.samrtcarduser.activates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samrtcarduser.R;
import com.example.samrtcarduser.adapter.HomeAdapter;
import com.example.samrtcarduser.adapter.NumberCardAdapter;
import com.example.samrtcarduser.adapter.TransactionAdapter;
import com.example.samrtcarduser.helper.HomeHelper;
import com.example.samrtcarduser.helper.TransactionItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private List<TransactionItem> list;
    private TextView tvNotFound;
    private ProgressBar progressBar;
    private DatabaseReference reference, reference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String strCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setTitle("History");

        initiateView();
        fullRecyclerView();

    }

    private void initiateView() {
        recyclerView = findViewById(R.id.recycler_transaction);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        tvNotFound = findViewById(R.id.tv_not_found);
        progressBar = findViewById(R.id.progress_bar);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        strCurrentUser = currentUser.getUid();
    }

    private void fullRecyclerView() {
        reference = FirebaseDatabase.getInstance().getReference("Transaction/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    String cardNumber = postSnapShot.child("cardNumber").getValue().toString();
                    String price = postSnapShot.child("price").getValue().toString();
                    String uid = postSnapShot.child("uid").getValue().toString();

                    if (uid.equals(strCurrentUser)) {
                        list.add(new TransactionItem(uid, cardNumber, price));
                    }
                }
                adapter = new TransactionAdapter(HistoryActivity.this, list);
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
                Toast.makeText(HistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}