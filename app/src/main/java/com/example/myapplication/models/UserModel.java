package com.example.myapplication.models;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int role;

    public UserModel(){}

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", id);
        userMap.put("fullName", fullName);
        userMap.put("email", email);
        userMap.put("phoneNumber", phoneNumber);
        userMap.put("role", role);
        return userMap;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
