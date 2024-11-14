package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.UserAdapter;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.sqlite.UserRepository;

import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<UserModel> userList;
    private Button addUserButton;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        // Setup toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("User Management");
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerViewUsers = findViewById(R.id.recycler_view_users);
        addUserButton = findViewById(R.id.add_user_button);

        userRepository = new UserRepository(this);
        userList = userRepository.getAllUsers();
        userAdapter = new UserAdapter(userList);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        addUserButton.setOnClickListener(v -> {
            startActivity(new Intent(UserManagementActivity.this, CreateAccountActivity.class));
        });

        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userRepository.getAllUsers());
        userAdapter.notifyDataSetChanged();
    }
}
