package com.example.samrtcarduser.activates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.samrtcarduser.R;

public class PayCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);

        getSupportActionBar().setTitle("Buy The Card");
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