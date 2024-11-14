package com.example.myapplication.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.BookingModel;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    private static final String TABLE_BOOKINGS = "bookings";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "userId";

    private SQLiteDatabase database;
    private SQLiteDatabaseHelper dbHelper;

    public BookingRepository(Context context) {
        dbHelper = new SQLiteDatabaseHelper(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    // Add a new booking
    public long addBooking(BookingModel booking) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, booking.getId());
        values.put(COLUMN_USER_ID, booking.getUserId());

        long result = database.insert(TABLE_BOOKINGS, null, values);
        close();
        return result;
    }

    // Update an existing booking
    public int updateBooking(BookingModel booking) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, booking.getUserId());

        int rowsAffected = database.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?", new String[]{booking.getId()});
        close();
        return rowsAffected;
    }

    // Delete a booking
    public void deleteBooking(String bookingId) {
        open();
        database.delete(TABLE_BOOKINGS, COLUMN_ID + " = ?", new String[]{bookingId});
        close();
    }

    // Get all bookings
    public List<BookingModel> getAllBookings() {
        List<BookingModel> bookingList = new ArrayList<>();
        open();

        String[] columns = {COLUMN_ID, COLUMN_USER_ID};
        Cursor cursor = database.query(TABLE_BOOKINGS, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                BookingModel booking = new BookingModel();
                booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return bookingList;
    }

    // Get a booking by ID
    public BookingModel getBookingById(String bookingId) {
        open();
        BookingModel booking = null;

        String[] columns = {COLUMN_ID, COLUMN_USER_ID};
        Cursor cursor = database.query(TABLE_BOOKINGS, columns, COLUMN_ID + " = ?", new String[]{bookingId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            booking = new BookingModel();
            booking.setId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            booking.setUserId(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            cursor.close();
        }

        close();
        return booking;
    }
}
