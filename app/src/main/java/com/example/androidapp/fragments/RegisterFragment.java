package com.example.androidapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.db.AppDatabase;
import com.example.androidapp.helpers.Helpers;
import com.example.androidapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {
    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvGoToLogin;
    private AppDatabase appDatabase;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    public static final String VALID_PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Z])(?=.*[a-z]).{8,}$";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAppDatabase();
        findViewById(view);
        onClick();
    }

    private void findViewById(View view) {
        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvGoToLogin = view.findViewById(R.id.tvGoToLogin);
    }

    private void onClick() {
        btnRegister.setOnClickListener(view1 -> {
            String username = Objects.requireNonNull(etUsername.getText()).toString().trim();
            String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
            String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
            String confirmPassword = Objects.requireNonNull(etConfirmPassword.getText()).toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(requireContext(), "Username cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty() || !Helpers.isValidEmail(etEmail.getText().toString())) {
                Toast.makeText(requireContext(), "Email cannot be empty or must be valid!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.matches(VALID_PASSWORD_PATTERN)) {
                Toast.makeText(requireContext(), "Password must contain at least 1 number, 1 special character, 1 uppercase letter, 1 lowercase letter, and be at least 8 characters long!", Toast.LENGTH_LONG).show();
                return;
            }
            if (confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Password confirmation cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(requireContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = new User(username, email, Helpers.hashPassword(password), LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                appDatabase.userDao().insertUser(user);
                mainThreadHandler.post(() -> {
                    Toast.makeText(requireContext(), "Register successfully!", Toast.LENGTH_SHORT).show();
                    showLoginFragment();
                });
            });

        });



        tvGoToLogin.setOnClickListener(view1 -> {
            showLoginFragment();
        });
    }

    private void showLoginFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }

    private void initAppDatabase() {
        appDatabase = AppDatabase.getInstance(requireContext());
    }




}