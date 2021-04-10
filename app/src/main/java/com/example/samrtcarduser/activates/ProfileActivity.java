package com.example.samrtcarduser.activates;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.samrtcarduser.R;
import com.example.samrtcarduser.utils.Validation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText edFirstName, edLastName, edEmail;
    private Button bnUpdateProfile;
    private TextView textViewName;
    private String strFirstName, strLastName, strEmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");

        initiateView();
        clickView();
        getProfileData();

    }

    private void getProfileData() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        uid = currentUser.getUid();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                strFirstName = snapshot.child(uid).child("firstName").getValue(String.class);
                strLastName = snapshot.child(uid).child("lastName").getValue(String.class);
                strEmail = snapshot.child(uid).child("email").getValue(String.class);

                fullProfileInfo(strFirstName, strLastName, strEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initiateView() {
        textViewName = findViewById(R.id.text_name_profile);
        edFirstName = findViewById(R.id.edittext_first_name_prifile);
        edLastName = findViewById(R.id.edittext_last_name_prifile);
        edEmail = findViewById(R.id.text_input_email);
        bnUpdateProfile = findViewById(R.id.bn_update_profile);

        edEmail.setInputType(InputType.TYPE_NULL);
        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(ProfileActivity.this, "It does not enable to modify the e-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clickView() {
        bnUpdateProfile.setOnClickListener(this);
        edEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_update_profile:
                if (!validateName(edFirstName) | !validateName(edLastName)) {
                    return;
                } else {
                    closeKeyboard(this);
                    updateProfile();
                }
                break;
            case R.id.text_input_email:
                Toast.makeText(ProfileActivity.this, "It does not enable to modify the e-mail", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void fullProfileInfo(String firstName, String laseName, String email) {
        edFirstName.setText(firstName);
        edLastName.setText(laseName);
        edEmail.setText(email);
        textViewName.setText(firstName + " " + laseName);
    }

    private void updateProfile() {
        String firstName = edFirstName.getText().toString().trim();
        String lastName = edLastName.getText().toString().trim();

        if (firstName.equals(strFirstName) && lastName.equals(strLastName)) {
            Toast.makeText(this, "No change happened", Toast.LENGTH_SHORT).show();
        } else {
            reference.child(uid).child("firstName").setValue(firstName);
            reference.child(uid).child("lastName").setValue(lastName);

            getProfileData();
        }
    }

    private boolean validateName(EditText editText) {
        return Validation.validName(this, editText);
    }

    private void closeKeyboard(Context context) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}