package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.activities.AddNewCourseActivity;
import com.example.myapplication.activities.BookingManagementActivity;
import com.example.myapplication.activities.CourseManagementActivity;
import com.example.myapplication.activities.UserManagementActivity;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.btnAddNewCourse).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddNewCourseActivity.class));
        });

        view.findViewById(R.id.btnCourseManagement).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CourseManagementActivity.class));
        });

        view.findViewById(R.id.btnUserManagement).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), UserManagementActivity.class));
        });

        view.findViewById(R.id.btnBookingManagement).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), BookingManagementActivity.class));
        });

        return view;
    }
}
