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
    private List<NumberCardHelper> listCardNumber;

    private TextView tvNotFound;
    private ProgressBar progressBar;

    private DatabaseReference reference;

    private Intent intent;
    private String child, price;

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

    }

    private void initiateView() {
        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");
        recyclerView = findViewById(R.id.recycler_all_number_Card);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        listCardNumber = new ArrayList<>();

        list = (List<NumberCardHelper>) intent.getSerializableExtra("LIST_TOTAL_CARD_PAID");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            listCardNumber.add(new NumberCardHelper(String.valueOf(list.get(i))));
        }

        adapter = new NumberCardAdapter(CardNumberActivity.this, listCardNumber);
        tvNotFound = findViewById(R.id.tv_not_found);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void fullRecyclerView() {
        recyclerView.setAdapter(adapter);
        if (listCardNumber.isEmpty()) {
            progressBar.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
