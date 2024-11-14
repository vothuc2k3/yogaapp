package com.example.myapplication.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.BookingClassModel;

import java.util.ArrayList;
import java.util.List;

public class BookingClassRepository {

    private static final String TABLE_BOOKING_CLASSES = "booking_classes";

    private static final String COLUMN_BOOKING_ID = "bookingId";
    private static final String COLUMN_CLASS_ID = "classId";

    private SQLiteDatabase database;
    private SQLiteDatabaseHelper dbHelper;

    public BookingClassRepository(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public long addBookingClass(BookingClassModel bookingClass) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKING_ID, bookingClass.getBookingId());
        values.put(COLUMN_CLASS_ID, bookingClass.getClassId());

        long result = database.insert(TABLE_BOOKING_CLASSES, null, values);
        close();
        return result;
    }

    public int updateBookingClass(BookingClassModel bookingClass) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_ID, bookingClass.getClassId());

        int rowsAffected = database.update(TABLE_BOOKING_CLASSES, values, COLUMN_BOOKING_ID + " = ?", new String[]{bookingClass.getBookingId()});
        close();
        return rowsAffected;
    }

    public void deleteBookingClassByBookingId(String bookingId) {
        open();
        database.delete(TABLE_BOOKING_CLASSES, COLUMN_BOOKING_ID + " = ?", new String[]{bookingId});
        close();
    }

    public void deleteBookingClassByClassId(String classId) {
        open();
        database.delete(TABLE_BOOKING_CLASSES, COLUMN_CLASS_ID + " = ?", new String[]{classId});
        close();
    }

    public List<BookingClassModel> getAllBookingClasses() {
        List<BookingClassModel> bookingClassList = new ArrayList<>();
        open();

        String[] columns = {COLUMN_BOOKING_ID, COLUMN_CLASS_ID};
        Cursor cursor = database.query(TABLE_BOOKING_CLASSES, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BookingClassModel bookingClass = new BookingClassModel();
                bookingClass.setBookingId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_ID)));
                bookingClass.setClassId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLASS_ID)));

                bookingClassList.add(bookingClass);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return bookingClassList;
    }

    public List<String> getClassIdsForBooking(String bookingId) {
        List<String> classIds = new ArrayList<>();
        open();

        String[] columns = { COLUMN_CLASS_ID };
        Cursor cursor = database.query(TABLE_BOOKING_CLASSES, columns, "bookingId = ?", new String[]{bookingId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                classIds.add(cursor.getString(cursor.getColumnIndexOrThrow("classId")));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return classIds;
    }
}
