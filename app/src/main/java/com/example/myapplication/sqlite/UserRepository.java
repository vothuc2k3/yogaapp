package com.example.myapplication.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    final private String TABLE_USERS = "users";

    final private String COLUMN_USER_ID = "id";
    final private String COLUMN_FULL_NAME = "fullName";
    final private String COLUMN_EMAIL = "email";
    final private String COLUMN_PHONE_NUMBER = "phoneNumber";
    final private String COLUMN_ROLE = "role";

    private SQLiteDatabase database;
    private SQLiteDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public List<UserModel> getAllTeachers() {
        List<UserModel> teacherList = new ArrayList<>();
        open();

        String[] columns = {
                COLUMN_USER_ID, COLUMN_FULL_NAME, COLUMN_EMAIL, COLUMN_PHONE_NUMBER, COLUMN_ROLE
        };

        Cursor cursor = database.query(TABLE_USERS, columns, COLUMN_ROLE + " = ?", new String[]{"1"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                userModel.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)));
                userModel.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                userModel.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
                userModel.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));

                teacherList.add(userModel);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return teacherList;
    }

    public long addUser(UserModel userModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userModel.getId());
        values.put(COLUMN_FULL_NAME, userModel.getFullName());
        values.put(COLUMN_EMAIL, userModel.getEmail());
        values.put(COLUMN_PHONE_NUMBER, userModel.getPhoneNumber());
        values.put(COLUMN_ROLE, userModel.getRole());

        long result = database.insert(TABLE_USERS, null, values);
        close();
        return result;
    }

    public int updateUser(UserModel userModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FULL_NAME, userModel.getFullName());
        values.put(COLUMN_EMAIL, userModel.getEmail());
        values.put(COLUMN_PHONE_NUMBER, userModel.getPhoneNumber());
        values.put(COLUMN_ROLE, userModel.getRole());

        int rowsAffected = database.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{userModel.getId()});
        close();
        return rowsAffected;
    }

    public void deleteUser(String userId) {
        open();
        database.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{userId});
        close();
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        open();

        String[] columns = {
                COLUMN_USER_ID, COLUMN_FULL_NAME, COLUMN_EMAIL, COLUMN_PHONE_NUMBER, COLUMN_ROLE
        };

        Cursor cursor = database.query(TABLE_USERS, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                userModel.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)));
                userModel.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                userModel.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
                userModel.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));

                userList.add(userModel);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return userList;
    }

    public UserModel getUserById(String userId) {
        open();
        UserModel userModel = null;

        String[] columns = {
                COLUMN_USER_ID, COLUMN_FULL_NAME, COLUMN_EMAIL, COLUMN_PHONE_NUMBER, COLUMN_ROLE
        };

        Cursor cursor = database.query(TABLE_USERS, columns, COLUMN_USER_ID + " = ?", new String[]{userId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            userModel = new UserModel();
            userModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            userModel.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)));
            userModel.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            userModel.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)));
            userModel.setRole(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE)));

            cursor.close();
        }

        close();
        return userModel;
    }
}
