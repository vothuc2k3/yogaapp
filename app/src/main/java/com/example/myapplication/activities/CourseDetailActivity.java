package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ClassAdapter;
import com.example.myapplication.models.ClassModel;
import com.example.myapplication.models.CourseModel;
import com.example.myapplication.sqlite.ClassRepository;
import com.example.myapplication.sqlite.CourseRepository;
import com.example.myapplication.sqlite.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView tvCourseStartDate, tvCourseStartTime, tvCourseCapacity, tvCourseType, tvCourseDuration, tvCourseClassesCount, tvCoursePrice    ;
    private RecyclerView rvClassesList;
    private ClassAdapter classAdapter;
    private CourseModel course;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        String courseId = getIntent().getStringExtra("courseId");

        userRepository = new UserRepository(this);

        if (courseId == null) {
            Toast.makeText(this, "Course not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvCourseStartDate = findViewById(R.id.tvCourseStartDate);
        tvCourseStartTime = findViewById(R.id.tvCourseStartTime);
        tvCourseCapacity = findViewById(R.id.tvCourseCapacity);
        tvCourseType = findViewById(R.id.tvCourseType);
        tvCourseDuration = findViewById(R.id.tvCourseDuration);
        tvCourseClassesCount = findViewById(R.id.tvCourseClassesCount);
        tvCoursePrice = findViewById(R.id.tvCoursePrice);
        rvClassesList = findViewById(R.id.rvClassesList);

        CourseRepository courseRepo = new CourseRepository(this);
        course = courseRepo.getCourseById(courseId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(course.getType());
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (course != null) {
            displayCourseDetails(course);
            loadClasses(courseId);
        } else {
            Toast.makeText(this, "Course not found!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayCourseDetails(CourseModel course) {
        tvCourseStartDate.setText("Start Date: " + formatDate(course.getStartAt()));
        tvCourseStartTime.setText("Start Time: " + course.getStartTime().toString());
        tvCourseCapacity.setText("Capacity: " + course.getCapacity());
        tvCourseType.setText("Type: " + course.getType());
        tvCourseDuration.setText("Duration: " + course.getDuration() + " minutes");
        tvCourseClassesCount.setText("Total Classes: " + course.getClassCount());
        tvCoursePrice.setText("Price: " + course.getPrice() + " $");
    }

    private void loadClasses(String courseId) {
        ClassRepository classRepo = new ClassRepository(this);
        List<ClassModel> classList = classRepo.getAllCourseClasses(courseId);

        classAdapter = new ClassAdapter(classList, this);
        rvClassesList.setLayoutManager(new LinearLayoutManager(this));
        rvClassesList.setAdapter(classAdapter);
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
