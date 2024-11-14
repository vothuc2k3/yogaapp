package com.example.myapplication.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    // Database Version and Name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "yogaStudio.db";

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_CLASSES = "classes";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_BOOKING_CLASSES = "booking_classes";

    // Users Table columns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_FULLNAME = "fullName";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phoneNumber";
    private static final String COLUMN_USER_ROLE = "role";

    // Courses Table columns
    private static final String COLUMN_COURSE_ID = "id";
    private static final String COLUMN_COURSE_DAY_OF_WEEK = "dayOfWeek";
    private static final String COLUMN_COURSE_START_TIME = "startTime";
    private static final String COLUMN_COURSE_CAPACITY = "capacity";
    private static final String COLUMN_COURSE_DURATION = "duration";
    private static final String COLUMN_COURSE_CLASS_COUNT = "classCount";
    private static final String COLUMN_COURSE_PRICE = "price";
    private static final String COLUMN_COURSE_TYPE = "type";
    private static final String COLUMN_COURSE_START_AT = "startAt";
    private static final String COLUMN_COURSE_END_AT = "endAt";

    // Classes Table columns
    private static final String COLUMN_CLASS_ID = "id";
    private static final String COLUMN_CLASS_COURSE_ID = "courseId";
    private static final String COLUMN_CLASS_TEACHER_ID = "teacherId";
    private static final String COLUMN_CLASS_START_DATE = "startDate";
    private static final String COLUMN_CLASS_PRICE = "price";

    // Bookings Table columns
    private static final String COLUMN_BOOKING_ID = "id";
    private static final String COLUMN_BOOKING_USER_ID = "userId";

    // Booking Classes Table columns
    private static final String COLUMN_BOOKING_CLASS_BOOKING_ID = "bookingId";
    private static final String COLUMN_BOOKING_CLASS_CLASS_ID = "classId";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_FULLNAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PHONE + " TEXT,"
                + COLUMN_USER_ROLE + " INTEGER"
                + ")";

        // Create Courses Table
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_COURSE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_COURSE_DAY_OF_WEEK + " TEXT,"
                + COLUMN_COURSE_START_TIME + " TEXT,"
                + COLUMN_COURSE_CAPACITY + " INTEGER,"
                + COLUMN_COURSE_DURATION + " INTEGER,"
                + COLUMN_COURSE_CLASS_COUNT + " INTEGER,"
                + COLUMN_COURSE_PRICE + " INTEGER,"
                + COLUMN_COURSE_TYPE + " TEXT,"
                + COLUMN_COURSE_START_AT + " INTEGER,"
                + COLUMN_COURSE_END_AT + " INTEGER"
                + ")";

        // Create Classes Table
        String CREATE_CLASSES_TABLE = "CREATE TABLE " + TABLE_CLASSES + "("
                + COLUMN_CLASS_ID + " TEXT PRIMARY KEY,"
                + COLUMN_CLASS_COURSE_ID + " TEXT,"
                + COLUMN_CLASS_START_DATE + " LONG,"
                + COLUMN_CLASS_TEACHER_ID + " TEXT,"
                + COLUMN_CLASS_PRICE + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_CLASS_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "),"
                + "FOREIGN KEY(" + COLUMN_CLASS_TEACHER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";

        // Create Bookings Table
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_BOOKING_ID + " TEXT PRIMARY KEY,"
                + COLUMN_BOOKING_USER_ID + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";

        // Create Booking Classes Table
        String CREATE_BOOKING_CLASSES_TABLE = "CREATE TABLE " + TABLE_BOOKING_CLASSES + "("
                + COLUMN_BOOKING_CLASS_BOOKING_ID + " TEXT,"
                + COLUMN_BOOKING_CLASS_CLASS_ID + " TEXT,"
                + "PRIMARY KEY(" + COLUMN_BOOKING_CLASS_BOOKING_ID + ", " + COLUMN_BOOKING_CLASS_CLASS_ID + "),"
                + "FOREIGN KEY(" + COLUMN_BOOKING_CLASS_BOOKING_ID + ") REFERENCES " + TABLE_BOOKINGS + "(" + COLUMN_BOOKING_ID + "),"
                + "FOREIGN KEY(" + COLUMN_BOOKING_CLASS_CLASS_ID + ") REFERENCES " + TABLE_CLASSES + "(" + COLUMN_CLASS_ID + ")"
                + ")";

        // Execute the SQL statements to create tables
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_CLASSES_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);
        db.execSQL(CREATE_BOOKING_CLASSES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING_CLASSES);

        // Create tables again
        onCreate(db);
    }
}
