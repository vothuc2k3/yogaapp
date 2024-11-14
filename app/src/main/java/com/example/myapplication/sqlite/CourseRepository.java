package com.example.myapplication.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.myapplication.models.CourseModel;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CourseRepository {

    private SQLiteDatabase database;
    private SQLiteDatabaseHelper dbHelper;
    private ClassRepository classRepo;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CourseRepository(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
        classRepo = new ClassRepository(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public long addCourse(CourseModel course) {
        open();
        ContentValues values = new ContentValues();
        values.put("id", course.getId());
        values.put("dayOfWeek", course.getDayOfWeek());
        values.put("startTime", course.getStartTime().toString());
        values.put("capacity", course.getCapacity());
        values.put("duration", course.getDuration());
        values.put("classCount", course.getClassCount());
        values.put("price", course.getPrice());
        values.put("type", course.getType());
        values.put("startAt", course.getStartAt());
        values.put("endAt", course.getEndAt());

        long insertId = database.insert("courses", null, values);
        close();
        return insertId;
    }

    public void updateCourse(CourseModel course) {
        open();
        ContentValues values = new ContentValues();
        values.put("dayOfWeek", course.getDayOfWeek());
        values.put("startTime", course.getStartTime().toString());
        values.put("capacity", course.getCapacity());
        values.put("duration", course.getDuration());
        values.put("classCount", course.getClassCount());
        values.put("price", course.getPrice());
        values.put("type", course.getType());
        values.put("startAt", course.getStartAt());
        values.put("endAt", course.getEndAt());

        database.update("courses", values, "id = ?", new String[]{course.getId()});
        close();
        return;
    }

    public void deleteCourse(String courseId) {
        open();
        database.delete("courses", "id = ?", new String[]{courseId});
        close();
        db.collection("courses").document(courseId).delete();
        classRepo.deleteClassesByCourseId(courseId);
    }

    public List<CourseModel> getAllCourses() {
        List<CourseModel> courses = new ArrayList<>();
        open();

        String[] columns = {
                "id", "dayOfWeek", "startTime", "capacity", "duration", "classCount", "price", "type", "startAt", "endAt"
        };

        Cursor cursor = database.query("courses", columns, null, null, null, null, null);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        if (cursor != null && cursor.moveToFirst()) {
            do {
                CourseModel course = new CourseModel();
                course.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                course.setDayOfWeek(cursor.getString(cursor.getColumnIndexOrThrow("dayOfWeek")));

                String startTimeString = cursor.getString(cursor.getColumnIndexOrThrow("startTime"));
                try {
                    java.util.Date parsedTime = timeFormat.parse(startTimeString);
                    if (parsedTime != null) {
                        course.setStartTime(new Time(parsedTime.getTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    course.setStartTime(null);
                }

                course.setCapacity(cursor.getInt(cursor.getColumnIndexOrThrow("capacity")));
                course.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow("duration")));
                course.setClassCount(cursor.getInt(cursor.getColumnIndexOrThrow("classCount")));
                course.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
                course.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                course.setStartAt(cursor.getLong(cursor.getColumnIndexOrThrow("startAt")));
                course.setEndAt(cursor.getLong(cursor.getColumnIndexOrThrow("endAt")));

                courses.add(course);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return courses;
    }

    public int getClassCountByCourseId(String courseId) {
        open();

        String query = "SELECT COUNT(*) FROM classes WHERE courseId = ?";
        Cursor cursor = database.rawQuery(query, new String[]{courseId});

        int classCount = 0;
        if (cursor != null && cursor.moveToFirst()) {
            classCount = cursor.getInt(0);
            cursor.close();
        }

        close();
        return classCount;
    }

    public CourseModel getCourseById(String courseId) {
        open();
        CourseModel course = null;

        String[] columns = {
                "id", "dayOfWeek", "startTime", "capacity", "duration", "classCount", "price", "type", "startAt", "endAt"
        };

        Cursor cursor = database.query("courses", columns, "id = ?", new String[]{courseId}, null, null, null);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        if (cursor != null && cursor.moveToFirst()) {
            course = new CourseModel();
            course.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
            course.setDayOfWeek(cursor.getString(cursor.getColumnIndexOrThrow("dayOfWeek")));

            String startTimeString = cursor.getString(cursor.getColumnIndexOrThrow("startTime"));
            try {
                java.util.Date parsedTime = timeFormat.parse(startTimeString);
                if (parsedTime != null) {
                    course.setStartTime(new Time(parsedTime.getTime()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
                course.setStartTime(null);
            }

            course.setCapacity(cursor.getInt(cursor.getColumnIndexOrThrow("capacity")));
            course.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow("duration")));
            course.setClassCount(cursor.getInt(cursor.getColumnIndexOrThrow("classCount")));
            course.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
            course.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
            course.setStartAt(cursor.getLong(cursor.getColumnIndexOrThrow("startAt")));
            course.setEndAt(cursor.getLong(cursor.getColumnIndexOrThrow("endAt")));

            cursor.close();
        }

        close();
        return course;
    }

}
