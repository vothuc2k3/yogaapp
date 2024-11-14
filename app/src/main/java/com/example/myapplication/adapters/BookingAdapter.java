package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.BookingModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.models.ClassModel;
import com.example.myapplication.sqlite.BookingClassRepository;
import com.example.myapplication.sqlite.UserRepository;
import com.example.myapplication.sqlite.ClassRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<BookingModel> bookingList;
    private UserRepository userRepository;
    private ClassRepository classRepository;
    private BookingClassRepository bookingClassRepository;

    public BookingAdapter(Context context, List<BookingModel> bookingList, UserRepository userRepository, ClassRepository classRepository, BookingClassRepository bookingClassRepository) {
        this.context = context;
        this.bookingList = bookingList;
        this.userRepository = userRepository;
        this.classRepository = classRepository;
        this.bookingClassRepository = bookingClassRepository;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        UserModel user = userRepository.getUserById(booking.getUserId());
        holder.tvBookingUserName.setText(user.getFullName());

        List<String> bookedClassIds = bookingClassRepository.getClassIdsForBooking(booking.getId());
        List<ClassModel> bookedClasses = new ArrayList<>();

        for (String classId : bookedClassIds) {
            ClassModel classModel = classRepository.getClassById(classId);
            if (classModel != null) {
                bookedClasses.add(classModel);
            }
        }
        ClassAdapter classAdapter = new ClassAdapter(bookedClasses, context);
        holder.rvBookedClasses.setLayoutManager(new LinearLayoutManager(context));
        holder.rvBookedClasses.setAdapter(classAdapter);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView tvBookingUserName;
        RecyclerView rvBookedClasses;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingUserName = itemView.findViewById(R.id.tvBookingUserName);
            rvBookedClasses = itemView.findViewById(R.id.rvBookedClasses);
        }
    }
}
