package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.models.UserModel;
import com.example.myapplication.sqlite.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;
    private TextView profileRole;
    private Button editProfileButton;
    private Button logoutButton;
    private UserRepository userRepository;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        logoutButton = view.findViewById(R.id.logout_button);
        profilePhone = view.findViewById(R.id.profile_phone);
        profileRole = view.findViewById(R.id.profile_role);

        userRepository = new UserRepository(getContext());
        UserModel user = userRepository.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mapTextViews(user);

        return view;
    }

    private void mapTextViews(UserModel user) {
        profileName.setText(user.getFullName());
        profileEmail.setText(user.getEmail());
        profilePhone.setText(user.getPhoneNumber());
        profileRole.setText(user.getRole() == 1 ? "Admin" : "Teacher");

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });
    }
}
