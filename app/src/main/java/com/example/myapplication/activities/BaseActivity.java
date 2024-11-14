package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent;
        if (currentUser != null) {
            intent = new Intent(BaseActivity.this, MainActivity.class);
        } else {
            intent = new Intent(BaseActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
