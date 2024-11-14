package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.ClassModel;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.sqlite.ClassRepository;
import com.example.myapplication.sqlite.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassModel> classList;
    final private UserRepository userRepository;
    final private ClassRepository classRepository;
    final private Context context;
    public ClassAdapter(List<ClassModel> classList, Context context) {
        this.classList = classList;
        this.userRepository = new UserRepository(context);
        this.classRepository = new ClassRepository(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassModel classModel = classList.get(position);

        UserModel teacher = userRepository.getUserById(classModel.getTeacherId());

        holder.tvClassDate.setText("Date: " + formatDate(classModel.getStartDate()));
        holder.tvTeacherName.setText("Teacher: " + teacher.getFullName());
        holder.tvClassPrice.setText("Price: " + classModel.getPrice() + " $");

        holder.btnEdit.setOnClickListener(v -> {
            editClass(classModel);
        });

        holder.btnDelete.setOnClickListener(v -> {
            deleteClass(classModel, position);
        });
    }

    private void deleteClass(ClassModel classModel, int position) {
        classRepository.deleteClass(classModel.getId());
        classList.remove(position);
        notifyItemRemoved(position);
    }

    private void editClass(ClassModel classModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Class");

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_class, null);
        builder.setView(dialogView);

        EditText editTeacherId = dialogView.findViewById(R.id.editTeacherId);
        EditText editStartDate = dialogView.findViewById(R.id.editStartDate);

        UserModel teacher = userRepository.getUserById(classModel.getTeacherId());
        editTeacherId.setText(teacher.getFullName());
        editStartDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(classModel.getStartDate()));

        editTeacherId.setOnClickListener(v -> {
            List<UserModel> teacherList = userRepository.getAllTeachers();
            List<String> teacherNames = new ArrayList<>();
            for (UserModel t : teacherList) {
                teacherNames.add(t.getFullName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, teacherNames);
            AlertDialog.Builder teacherDialogBuilder = new AlertDialog.Builder(context);
            teacherDialogBuilder.setAdapter(adapter, (dialog, which) -> {
                UserModel selectedTeacher = teacherList.get(which);
                editTeacherId.setText(selectedTeacher.getFullName());
                classModel.setTeacherId(selectedTeacher.getId());
            });
            teacherDialogBuilder.show();
        });

        builder.setPositiveButton("Save", (dialog, which) -> {
            try {
                classModel.setStartDate(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(editStartDate.getText().toString().trim()).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            classRepository.updateClass(classModel);
            notifyItemChanged(classList.indexOf(classModel));
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView tvClassDate, tvTeacherName, tvClassPrice, btnEdit, btnDelete;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassDate = itemView.findViewById(R.id.tvClassDate);
            tvTeacherName = itemView.findViewById(R.id.tvTeacherName);
            tvClassPrice = itemView.findViewById(R.id.tvClassPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void filterList(List<ClassModel> filteredList) {
        classList = filteredList;
        notifyDataSetChanged();
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
