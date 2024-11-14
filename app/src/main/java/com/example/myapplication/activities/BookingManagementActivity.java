package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.BookingAdapter;
import com.example.myapplication.models.BookingModel;
import com.example.myapplication.sqlite.BookingClassRepository;
import com.example.myapplication.sqlite.BookingRepository;
import com.example.myapplication.sqlite.ClassRepository;
import com.example.myapplication.sqlite.UserRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class BookingManagementActivity extends AppCompatActivity {

    private RecyclerView rvBookingList;
    private BookingAdapter bookingAdapter;
    private List<BookingModel> bookingList;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ClassRepository classRepository;
    private BookingClassRepository bookingClassRepository;
    private FloatingActionButton fabAddBooking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_management);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Booking Management");
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        rvBookingList = findViewById(R.id.rvBookingList);
        rvBookingList.setLayoutManager(new LinearLayoutManager(this));

        bookingRepository = new BookingRepository(this);
        userRepository = new UserRepository(this);
        classRepository = new ClassRepository(this);
        bookingClassRepository = new BookingClassRepository(this);

        bookingList = bookingRepository.getAllBookings();

        bookingAdapter = new BookingAdapter(this, bookingList, userRepository, classRepository, bookingClassRepository);
        rvBookingList.setAdapter(bookingAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookingList.clear();
        bookingList.addAll(bookingRepository.getAllBookings());
        bookingAdapter.notifyDataSetChanged();
    }
}
