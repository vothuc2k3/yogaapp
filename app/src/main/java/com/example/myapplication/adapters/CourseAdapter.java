package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.activities.CourseDetailActivity;
import com.example.myapplication.models.ClassModel;
import com.example.myapplication.models.CourseModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.sqlite.ClassRepository;
import com.example.myapplication.sqlite.CourseRepository;
import com.example.myapplication.sqlite.UserRepository;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final Context context;
    private final List<CourseModel> courseList;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseAdapter(Context context, List<CourseModel> courseList, CourseRepository courseRepository, UserRepository userRepository) {
        this.context = context;
        this.courseList = courseList;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseModel course = courseList.get(position);

        holder.className.setText(course.getType());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.startTimeValue.setText(timeFormat.format(course.getStartTime()));
        holder.capacityValue.setText("Capacity: " + course.getCapacity());
        holder.sessionCountValue.setText("Sessions: " + course.getClassCount());

        int actualClassCount = courseRepository.getClassCountByCourseId(course.getId());

        if (actualClassCount < course.getClassCount()) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFCDD2"));
            holder.tvSessionWarning.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose an option");

            List<String> options = new ArrayList<>();
            options.add("View Class Detail");
            options.add("Edit Class");
            options.add("Delete Class");

            if (actualClassCount < course.getClassCount()) {
                options.add("Add Class");
            }

            String[] optionsArray = options.toArray(new String[0]);

            builder.setItems(optionsArray, (dialog, which) -> {
                String selectedOption = optionsArray[which];
                switch (selectedOption) {
                    case "View Class Detail":
                        viewCourseDetail(course);
                        break;
                    case "Edit Class":
                        editClass(course);
                        break;
                    case "Delete Class":
                        courseRepository.deleteCourse(course.getId());
                        courseList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        break;
                    case "Add Class":
                        addClasses(course);
                        break;
                }
            });
            builder.show();
        });
    }

    private void viewCourseDetail(CourseModel course) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra("courseId", course.getId());
        context.startActivity(intent);
    }

    private void editClass(CourseModel course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Course");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_course, null);
        builder.setView(dialogView);

        EditText editDayOfWeek = dialogView.findViewById(R.id.editDayOfWeek);
        EditText editStartTime = dialogView.findViewById(R.id.editStartTime);
        EditText editStartDate = dialogView.findViewById(R.id.editStartDate);
        EditText editCapacity = dialogView.findViewById(R.id.editCapacity);
        EditText editDuration = dialogView.findViewById(R.id.editDuration);
        EditText editClassCount = dialogView.findViewById(R.id.editClassCount);
        EditText editPrice = dialogView.findViewById(R.id.editPrice);
        EditText editType = dialogView.findViewById(R.id.editType);

        editDayOfWeek.setText(course.getDayOfWeek());
        editStartTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(course.getStartTime()));
        editStartDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(course.getStartAt()));
        editCapacity.setText(String.valueOf(course.getCapacity()));
        editDuration.setText(String.valueOf(course.getDuration()));
        editClassCount.setText(String.valueOf(course.getClassCount()));
        editPrice.setText(String.valueOf(course.getPrice()));
        editType.setText(course.getType());

        builder.setPositiveButton("Save", (dialog, which) -> {
            course.setDayOfWeek(editDayOfWeek.getText().toString().trim());
            try {
                course.setStartTime(new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(editStartTime.getText().toString().trim()).getTime()));
                course.setStartAt(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(editStartDate.getText().toString().trim()).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            course.setCapacity(Integer.parseInt(editCapacity.getText().toString().trim()));
            course.setDuration(Integer.parseInt(editDuration.getText().toString().trim()));
            course.setClassCount(Integer.parseInt(editClassCount.getText().toString().trim()));
            course.setPrice(Integer.parseInt(editPrice.getText().toString().trim()));
            course.setType(editType.getText().toString().trim());

            courseRepository.updateCourse(course);
            notifyItemChanged(courseList.indexOf(course));
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addClasses(CourseModel course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add New Classes");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_class, null);
        builder.setView(dialogView);

        Spinner spinnerTeacher = dialogView.findViewById(R.id.spinnerTeacher);

        List<UserModel> teacherList = userRepository.getAllTeachers();

        ArrayAdapter<UserModel> adapter = getUserModelArrayAdapter(teacherList);
        spinnerTeacher.setAdapter(adapter);

        builder.setPositiveButton("Add", (dialog, which) -> {
            int selectedPosition = spinnerTeacher.getSelectedItemPosition();
            UserModel selectedTeacher = teacherList.get(selectedPosition);

            ClassRepository classRepository = new ClassRepository(context);
            List<ClassModel> existingClasses = classRepository.getAllCourseClasses(course.getId());
            int existingClassCount = existingClasses.size();

            int totalClassesToAdd = course.getClassCount() - existingClassCount;
            if (totalClassesToAdd <= 0) {
                Toast.makeText(context, "No classes to add!", Toast.LENGTH_SHORT).show();
                return;
            }

            long courseStartAt = course.getStartAt();
            int daysBetweenClasses = 7;

            for (int i = 0; i < totalClassesToAdd; i++) {
                long newClassStartDate = courseStartAt + ((existingClassCount + i) * daysBetweenClasses * 24 * 60 * 60 * 1000L);

                ClassModel newClass = new ClassModel();
                newClass.setId(UUID.randomUUID().toString());
                newClass.setCourseId(course.getId());
                newClass.setTeacherId(selectedTeacher.getId());
                newClass.setPrice(course.getPrice());
                newClass.setStartDate(newClassStartDate);
                classRepository.addClass(newClass);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private @NonNull ArrayAdapter<UserModel> getUserModelArrayAdapter(List<UserModel> teacherList) {
        ArrayAdapter<UserModel> adapter = new ArrayAdapter<UserModel>(context, android.R.layout.simple_spinner_item, teacherList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setText(teacherList.get(position).getFullName());
                return label;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setText(teacherList.get(position).getFullName());
                return label;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView className, startTimeValue, capacityValue, sessionCountValue, tvSessionWarning;
        CardView cardView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            className = itemView.findViewById(R.id.class_name);
            startTimeValue = itemView.findViewById(R.id.start_time_value);
            capacityValue = itemView.findViewById(R.id.capacity_value);
            sessionCountValue = itemView.findViewById(R.id.session_count_value);
            tvSessionWarning = itemView.findViewById(R.id.tv_session_warning);
            cardView = itemView.findViewById(R.id.cardCourse);
        }
    }
}
