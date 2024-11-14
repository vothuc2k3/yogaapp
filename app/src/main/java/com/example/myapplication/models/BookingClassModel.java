package com.example.myapplication.models;

public class BookingClassModel {
    private String bookingId;
    private String classId;

    public BookingClassModel(){}

    public BookingClassModel(String bookingId, String classId) {
        this.bookingId = bookingId;
        this.classId = classId;
    }


    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
