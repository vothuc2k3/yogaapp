package com.example.myapplication.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.CourseModel;
import com.example.myapplication.sqlite.CourseRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.widget.Toolbar;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddNewCourseActivity extends AppCompatActivity {

    private TextInputEditText startDateEditText, startTimeEditText, capacityEditText, durationEditText, classCountEditText, typeEditText, dayOfWeekEditText, priceEditText;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add New Course");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dayOfWeekEditText = findViewById(R.id.dayOfWeekEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        capacityEditText = findViewById(R.id.capacityEditText);
        durationEditText = findViewById(R.id.durationEditText);
        classCountEditText = findViewById(R.id.classCountEditText);
        typeEditText = findViewById(R.id.typeEditText);
        priceEditText = findViewById(R.id.priceEditText);
        btnSave = findViewById(R.id.btnSave);

        typeEditText.setOnClickListener(v -> showClassTypeDialog());
        dayOfWeekEditText.setOnClickListener(v -> showDayOfWeekDialog());
        startTimeEditText.setOnClickListener(v -> showTimePicker());
        startDateEditText.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> saveNewCourse());
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            if (hourOfDay >= 6 && hourOfDay <= 20) {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                startTimeEditText.setText(time);
            } else {
                Toast.makeText(this, "Please select a time between 6:00 AM and 8:00 PM", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.updateTime(6, 0);
        timePickerDialog.show();
    }

    private void showClassTypeDialog() {
        String[] classTypes = getResources().getStringArray(R.array.class_type_array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Class Type");

        builder.setItems(classTypes, (dialog, which) -> {
            typeEditText.setText(classTypes[which]);
        });

        builder.show();
    }

    private void showDayOfWeekDialog() {
        String[] daysOfWeek = getResources().getStringArray(R.array.days_of_week_array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Day of the Week");

        builder.setItems(daysOfWeek, (dialog, which) -> dayOfWeekEditText.setText(daysOfWeek[which]));

        builder.show();
    }

    private void showDatePicker() {
        String dayOfWeek = dayOfWeekEditText.getText().toString().trim();

        if (TextUtils.isEmpty(dayOfWeek)) {
            Toast.makeText(this, "Please select the day of the week first", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);

            int selectedDayOfWeek = getDayOfWeekInt(dayOfWeek);
            if (calendar.get(Calendar.DAY_OF_WEEK) != selectedDayOfWeek) {
                Toast.makeText(this, "Please select a date that falls on a " + dayOfWeek, Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            startDateEditText.setText(dateFormat.format(calendar.getTime()));

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        int selectedDayOfWeek = getDayOfWeekInt(dayOfWeek);
        while (calendar.get(Calendar.DAY_OF_WEEK) != selectedDayOfWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private int getDayOfWeekInt(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Sunday": return Calendar.SUNDAY;
            case "Monday": return Calendar.MONDAY;
            case "Tuesday": return Calendar.TUESDAY;
            case "Wednesday": return Calendar.WEDNESDAY;
            case "Thursday": return Calendar.THURSDAY;
            case "Friday": return Calendar.FRIDAY;
            case "Saturday": return Calendar.SATURDAY;
            default: return -1;
        }
    }

    private void saveNewCourse() {
        String dayOfWeek = dayOfWeekEditText.getText().toString().trim();
        String startTimeStr = startTimeEditText.getText().toString().trim();
        String startDateStr = startDateEditText.getText().toString().trim();
        String capacityStr = capacityEditText.getText().toString().trim();
        String durationStr = durationEditText.getText().toString().trim();
        String classCountStr = classCountEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String type = typeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(dayOfWeek) || TextUtils.isEmpty(startTimeStr) || TextUtils.isEmpty(startDateStr) ||
                TextUtils.isEmpty(capacityStr) || TextUtils.isEmpty(durationStr) || TextUtils.isEmpty(classCountStr) ||
                TextUtils.isEmpty(type) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int capacity = Integer.parseInt(capacityStr);
        int duration = Integer.parseInt(durationStr);
        int classCount = Integer.parseInt(classCountStr);
        int price = Integer.parseInt(priceStr);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Time startTime;
        try {
            Date parsedTime = timeFormat.parse(startTimeStr);
            startTime = new Time(parsedTime.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        long startDateInMillis;
        try {
            Date parsedDate = dateFormat.parse(startDateStr);
            startDateInMillis = parsedDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();

        CourseModel newCourse = new CourseModel();

        newCourse.setId(id);
        newCourse.setDayOfWeek(dayOfWeek);
        newCourse.setStartAt(startDateInMillis);
        newCourse.setStartTime(startTime);
        newCourse.setCapacity(capacity);
        newCourse.setDuration(duration);
        newCourse.setClassCount(classCount);
        newCourse.setType(type);
        newCourse.setPrice(price);

        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000;
        long endAt = startDateInMillis + (classCount - 1) * oneWeekInMillis;

        newCourse.setEndAt(endAt);

        CourseRepository courseRepo = new CourseRepository(this);
        long result = courseRepo.addCourse(newCourse);

        if (result > 0) {
            Toast.makeText(this, "New course added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding course", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

}
