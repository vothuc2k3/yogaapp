package com.example.myapplication.models;

import java.util.HashMap;
import java.util.Map;

public class ClassModel {
    private String id;
    private String courseId;
    private String  teacherId;
    private long startDate;
    private int price;

    public ClassModel (){}

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("courseId", courseId);
        map.put("teacherId", teacherId);
        map.put("startDate", startDate);
        map.put("price", price);
        return map;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }
}
