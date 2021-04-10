package com.example.samrtcarduser.activates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samrtcarduser.R;

import com.example.samrtcarduser.adapter.HomeAdapter;
import com.example.samrtcarduser.helper.HomeHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<HomeHelper> list;


    private TextView tvNotFound, tvName;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private DatabaseReference reference, referenceProfile;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String uid;
    private String strFirstName, strLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home Page");

        initiateView();
        getProfileData();


    }


    private void initiateView() {
        recyclerView = findViewById(R.id.recycler_all_card);
        recyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();
        tvNotFound = findViewById(R.id.tv_not_found);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        View hView = navigationView.getHeaderView(0);
        tvName = hView.findViewById(R.id.tv_name);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        reference = FirebaseDatabase.getInstance().getReference("Card");
    }

    private void fullRecyclerView() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    String name = postSnapShot.getKey();
                    String imageView = postSnapShot.child("picCard").getValue().toString();

                    list.add(new HomeHelper(name, imageView));
                }
                adapter = new HomeAdapter(HomeActivity.this, list);
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
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getProfileData() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        referenceProfile = FirebaseDatabase.getInstance().getReference("User");
        uid = currentUser.getUid();

        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                strFirstName = snapshot.child(uid).child("firstName").getValue(String.class);
                strLastName = snapshot.child(uid).child("lastName").getValue(String.class);


                fullProfileInfo(strFirstName, strLastName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fullProfileInfo(String firstName, String laseName) {
        tvName.setText(firstName + " " + laseName);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Are you sure you went to exist Smart Card")
                    .setCancelable(false)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            dialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        fullRecyclerView();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.item_sign_out:
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage("Are you sure you went to exist Smart Card")
                        .setCancelable(false)
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                dialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}