package com.example.myapplication.models;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class CourseModel {
    private String id;
    private String dayOfWeek;
    private Time startTime;
    private int capacity;
    private int duration;
    private int classCount;
    private int price;
    private String type;
    private long startAt;
    private long endAt;

    public CourseModel(){}

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("dayOfWeek", dayOfWeek);
        map.put("startTime", startTime != null ? startTime.toString() : null);
        map.put("capacity", capacity);
        map.put("duration", duration);
        map.put("classCount", classCount);
        map.put("price", price);
        map.put("type", type);
        map.put("startAt", startAt);
        map.put("endAt", endAt);
        return map;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }
}
