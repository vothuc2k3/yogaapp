package com.example.myapplication.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.myapplication.models.ClassModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClassRepository {
    final private String TABLE_CLASSES = "classes";

    final private String COLUMN_CLASS_ID = "id";
    final private String COLUMN_COURSE_ID = "courseId";
    final private String COLUMN_TEACHER_ID = "teacherId";
    final private String COLUMN_PRICE = "price";
    final private String COLUMN_START_DATE = "startDate";

    private SQLiteDatabase database;
    private final SQLiteDatabaseHelper dbHelper;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ClassRepository(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public ClassModel getClassById(String classId) {
        open();

        ClassModel classModel = null;

        Cursor cursor = database.query(TABLE_CLASSES, null, COLUMN_CLASS_ID + " = ?", new String[]{classId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            classModel = new ClassModel();
            classModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID)));
            classModel.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)));
            classModel.setTeacherId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID)));
            classModel.setStartDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)));
            classModel.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            cursor.close();
        }
        close();
        return classModel;
    }


    public void addClass(ClassModel classModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_ID, classModel.getId());
        values.put(COLUMN_COURSE_ID, classModel.getCourseId());
        values.put(COLUMN_TEACHER_ID, classModel.getTeacherId());
        values.put(COLUMN_PRICE, classModel.getPrice());
        values.put(COLUMN_START_DATE, classModel.getStartDate());

        database.insert(TABLE_CLASSES, null, values);
        close();
        return;
    }

    public int updateClass(ClassModel classModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_ID, classModel.getTeacherId());
        values.put(COLUMN_PRICE, classModel.getPrice());
        values.put(COLUMN_START_DATE, classModel.getStartDate());

        int rowsAffected = database.update(TABLE_CLASSES, values, COLUMN_CLASS_ID + " = ?", new String[]{classModel.getId()});
        close();
        return rowsAffected;
    }

    public void deleteClass(String classId) {
        open();
        database.delete(TABLE_CLASSES, COLUMN_CLASS_ID + " = ?", new String[]{classId});
        close();
        db.collection("classes").document(classId).delete();
    }

    public void deleteClassesByCourseId(String courseId) {
        open();
        database.delete(TABLE_CLASSES, COLUMN_COURSE_ID + " = ?", new String[]{courseId});
        close();
        db.collection("classes").whereEqualTo("courseId", courseId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                }
            }
        });
    }

    public List<ClassModel> getAllCourseClasses(String courseId) {
        List<ClassModel> classList = new ArrayList<>();
        open();

        String[] columns = {
                COLUMN_CLASS_ID, COLUMN_COURSE_ID, COLUMN_TEACHER_ID, COLUMN_PRICE, COLUMN_START_DATE
        };

        Cursor cursor = database.query(TABLE_CLASSES, columns, COLUMN_COURSE_ID + " = ?", new String[]{courseId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ClassModel classModel = new ClassModel();
                classModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID)));
                classModel.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)));
                classModel.setTeacherId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID)));
                classModel.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                classModel.setStartDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)));

                classList.add(classModel);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return classList;
    }

    public List<ClassModel> getAllClasses() {
        List<ClassModel> classList = new ArrayList<>();
        open();

        String[] columns = {
                COLUMN_CLASS_ID, COLUMN_COURSE_ID, COLUMN_TEACHER_ID, COLUMN_PRICE, COLUMN_START_DATE
        };

        Cursor cursor = database.query(TABLE_CLASSES, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ClassModel classModel = new ClassModel();
                classModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID)));
                classModel.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)));
                classModel.setTeacherId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID)));
                classModel.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                classModel.setStartDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)));

                classList.add(classModel);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return classList;
    }

}
