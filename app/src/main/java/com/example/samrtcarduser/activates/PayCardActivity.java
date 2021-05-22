package com.example.samrtcarduser.activates;


import androidx.annotation.NonNull;
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

import com.example.samrtcarduser.helper.TransactionItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PayCardActivity extends AppCompatActivity {


    private DatabaseReference reference, reference2;
    private SharedPreferences preferences;
    private String root, parent;
    private Intent intent;
    private String child, priceCard;

    private TextView tvCardName, tvCardCategory, tvCardType, tvCardPrice, tvCounter;
    private Button bnAdd, bnSub, bnPay;

    private Dialog dialogPay;
    private TextView tvTotalNumber, tvTotalPrice, tvPay;

    private String strCount = "0";
    private int intTotalPrice;

    private int intTotalCardNumber;
    List<String> listCardNumber = new ArrayList<>();

    private FirebaseAuth auth;
    private String currentUser;
    private DatabaseReference mReferenceTransaction;

    private TransactionItem transactionItem;
    private String cardNumber;
    private List<String> listKeyCardNumber = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        getSupportActionBar().setTitle("Buy The Card");


        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getUid();

        mReferenceTransaction = FirebaseDatabase.getInstance().getReference().child("Transaction");


        initiateView();

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

                switch (strCount) {
                    case "0":
                        strCount = "0";
                        intTotalPrice = 0;
                        break;
                    default:
                        intTotalPrice = intTotalPrice - Integer.parseInt(priceCard);
                        tvCounter.setText(String.valueOf(count - 1));
                        strCount = tvCounter.getText().toString().trim();
                        break;
                }
            }
        });

        bnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strCount.equals("0")) {
                    Toast.makeText(PayCardActivity.this, "Please add card", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(strCount) > intTotalCardNumber) {
                    Toast.makeText(PayCardActivity.this, "The total of cards is greater than the total available cards", Toast.LENGTH_SHORT).show();
                } else {
                    openDialogPay(strCount, intTotalPrice);
                }
            }
        });
    }

    private void openDialogPay(String count, final int totalPrice) {
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

                List<String> list = new ArrayList<>();
                for (int i = 0; i < Integer.parseInt(strCount); i++) {
                    list.add(listCardNumber.get(i));
                    deleteNumberPaid(listKeyCardNumber.get(i));
                }

                transactionItem = new TransactionItem(currentUser, list.toString(), String.valueOf(totalPrice));
                mReferenceTransaction.push().setValue(transactionItem);


                Intent intent = new Intent(PayCardActivity.this, CardNumberActivity.class);
                intent.putExtra("NAME_CARD", child);
                intent.putExtra("LIST_TOTAL_CARD_PAID", (Serializable) list);
                startActivity(intent);
                finish();
                dialogPay.dismiss();
            }
        });
        dialogPay.show();
    }

    private void deleteNumberPaid(String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child + "/" + cardNumber);
        reference.child(key).removeValue();
    }


    private void initiateView() {
        tvCardName = findViewById(R.id.tv_card_name);
        tvCardCategory = findViewById(R.id.tv_card_category);
        tvCardType = findViewById(R.id.tv_card_type);
        tvCardPrice = findViewById(R.id.tv_price);
        tvCounter = findViewById(R.id.tv_counter);
        bnAdd = findViewById(R.id.bn_add);
        bnSub = findViewById(R.id.bn_sub);
        bnPay = findViewById(R.id.bn_pay);
        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);
        parent = preferences.getString("PARENT", null);

        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");

        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child + "/");

        getPriceCard(reference);

    }

    private void getPriceCard(DatabaseReference reference) {

        reference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final String name = snapshot.getKey();
                            System.out.println("name " + name);
                            switch (name) {
                                case "price":
                                    priceCard = snapshot.getValue(String.class);
                                    fullCardInfo(priceCard);
                                    break;
                                case "CardNumber":
                                    cardNumber = name;
                                    reference2 = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child + "/" + name);
                                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                listKeyCardNumber.add(reference2.child(name).push().getKey());
                                                listCardNumber.add(child.getValue().toString());
                                                intTotalCardNumber += 1;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


    private void fullCardInfo(String priceCard) {
        tvCardName.setText(root + ".");
        tvCardCategory.setText(parent + ".");
        tvCardType.setText(child + ".");
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