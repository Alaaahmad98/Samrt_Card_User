package com.example.samrtcarduser.activates;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samrtcarduser.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PayCardActivity extends AppCompatActivity {


    private DatabaseReference reference;
    private SharedPreferences preferences;
    private String root, parent;
    private Intent intent;
    private String child, numberCard, priceCard;

    private TextView tvCardName, tvCardCategory, tvCardType, tvCardNumber, tvCardPrice, tvCounter;
    private Button bnAdd, bnSub, bnPay;

    private Dialog dialogPay;
    private TextView tvTotalNumber, tvTotalPrice, tvPay;

    private String strCount="0";
    private int intTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        getSupportActionBar().setTitle("Buy The Card");

        initiateView();
        fullCardInfo();

        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvCounter.getText().toString().trim());
                tvCounter.setText(String.valueOf(count + 1));
                strCount = tvCounter.getText().toString().trim();

                switch (strCount) {
                    case "0":
                        intTotalPrice = 0;
                        break;
                    default:
                        intTotalPrice = Integer.parseInt(priceCard) * Integer.parseInt(strCount);
                        break;
                }
            }
        });

        bnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvCounter.getText().toString().trim());
                if (count == 0) {
                    strCount = "0";
                } else {
                    tvCounter.setText(String.valueOf(count - 1));
                    strCount = tvCounter.getText().toString().trim();
                }
            }
        });

        bnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strCount.equals("0")) {
                    Toast.makeText(PayCardActivity.this, "Please Add Card", Toast.LENGTH_SHORT).show();
                } else {
                    openDialogPay(strCount, intTotalPrice);
                }
            }
        });
    }

    private void openDialogPay(String count, int totalPrice) {
        dialogPay = new Dialog(this);
        dialogPay.setContentView(R.layout.dialog_confirm);
        tvTotalNumber = dialogPay.findViewById(R.id.tv_total_number_card);
        tvTotalPrice = dialogPay.findViewById(R.id.tv_total_price_card);
        tvPay = dialogPay.findViewById(R.id.tv_pay);

        tvTotalNumber.setText(count);
        tvTotalPrice.setText(String.valueOf(totalPrice));

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialogPay.show();
    }


    private void initiateView() {
        tvCardName = findViewById(R.id.tv_card_name);
        tvCardCategory = findViewById(R.id.tv_card_category);
        tvCardType = findViewById(R.id.tv_card_type);
        tvCardPrice = findViewById(R.id.tv_price);
        tvCardNumber = findViewById(R.id.tv_number);
        tvCounter = findViewById(R.id.tv_counter);
        bnAdd = findViewById(R.id.bn_add);
        bnSub = findViewById(R.id.bn_sub);
        bnPay = findViewById(R.id.bn_pay);
        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);
        parent = preferences.getString("PARENT", null);

        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");
        numberCard = intent.getStringExtra("NUMBER_CARD");
        priceCard = intent.getStringExtra("NUMBER_PRICE");


        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child);
    }

    private void fullCardInfo() {
        tvCardName.setText(root + ".");
        tvCardCategory.setText(parent + ".");
        tvCardType.setText(child + ".");
        tvCardNumber.setText(numberCard + ".");
        tvCardPrice.setText(priceCard + " JD" + ".");
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