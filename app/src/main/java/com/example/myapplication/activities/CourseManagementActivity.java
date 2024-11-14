package com.example.myapplication.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.CourseAdapter;
import com.example.myapplication.models.CourseModel;
import com.example.myapplication.sqlite.CourseRepository;
import com.example.myapplication.sqlite.UserRepository;

import java.util.List;

public class CourseManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private List<CourseModel> courseList;
    private CourseRepository courseRepository;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_management);

        courseRepository = new CourseRepository(this);
        userRepository = new UserRepository(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Course Management");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));

        courseList = courseRepository.getAllCourses();
        courseAdapter = new CourseAdapter(this, courseList, courseRepository, userRepository);
        recyclerViewCourses.setAdapter(courseAdapter);
    }
}
