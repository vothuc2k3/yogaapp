package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.UserModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserModel> userList;

    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.fullNameTextView.setText(user.getFullName());
        holder.emailTextView.setText(user.getEmail());
        holder.phoneNumberTextView.setText(user.getPhoneNumber());

        String roleText;
        if (user.getRole() == 0) {
            roleText = "Admin";
        } else if (user.getRole() == 1) {
            roleText = "Teacher";
        } else {
            roleText = "Customer";
        }
        holder.roleTextView.setText(roleText);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView fullNameTextView;
        TextView emailTextView;
        TextView phoneNumberTextView;
        TextView roleTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.user_full_name);
            emailTextView = itemView.findViewById(R.id.user_email);
            phoneNumberTextView = itemView.findViewById(R.id.user_phone_number);
            roleTextView = itemView.findViewById(R.id.user_role);
        }
    }
}
