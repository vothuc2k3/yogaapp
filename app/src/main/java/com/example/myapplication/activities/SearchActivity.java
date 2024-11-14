package com.example.myapplication.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ClassAdapter;
import com.example.myapplication.models.ClassModel;
import com.example.myapplication.sqlite.ClassRepository;
import com.example.myapplication.sqlite.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView recyclerView;
    private ClassAdapter classAdapter;
    private List<ClassModel> classList;
    private ClassRepository classRepository;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Teacher");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        searchInput = findViewById(R.id.search_input);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userRepository = new UserRepository(this);
        classRepository = new ClassRepository(this);
        classList = classRepository.getAllClasses();

        classAdapter = new ClassAdapter(classList, this);
        recyclerView.setAdapter(classAdapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filter(String text) {
        List<ClassModel> filteredList = new ArrayList<>();
        for (ClassModel classModel : classList) {
            String teacherName = userRepository.getUserById(classModel.getTeacherId()).getFullName();
            if (teacherName != null && teacherName.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(classModel);
            }
        }
        classAdapter.filterList(filteredList);
    }

}
