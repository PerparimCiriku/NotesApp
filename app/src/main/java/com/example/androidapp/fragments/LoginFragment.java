package com.example.androidapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.androidapp.R;
import com.example.androidapp.db.AppDatabase;
import com.example.androidapp.helpers.Helpers;
import com.example.androidapp.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.Executors;


public class LoginFragment extends Fragment {
    private TextInputEditText etUsername, etPassword;
    private TextView tvForgotPassword, tvGoToRegister;
    private MaterialButton btnLogin;
    private AppDatabase appDatabase;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
        etPassword = view.findViewById(R.id.etPassword);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        tvGoToRegister = view.findViewById(R.id.tvGoToRegister);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    private void onClick() {
        btnLogin.setOnClickListener(view1 -> {
            String username = Objects.requireNonNull(etUsername.getText()).toString();
            String password = Objects.requireNonNull(etPassword.getText()).toString();

            if (username.isEmpty()) {
                Toast.makeText(requireContext(), "Username cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User currentUser = appDatabase.userDao().validateUser(username, Helpers.hashPassword(password));
                if (currentUser == null) {
                    mainThreadHandler.post(() -> Toast.makeText(requireContext(), "Username or password is not valid!", Toast.LENGTH_SHORT).show());
                } else {
                    mainThreadHandler.post(() -> {
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                        showHomeFragment(username);
                    });
                }
            });

        });
        tvForgotPassword.setOnClickListener(view1 -> {
            showForgotPasswordDialog();
        });

        tvGoToRegister.setOnClickListener(view1 -> {
            showRegisterFragment();
        });
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText emailInput = new EditText(requireContext());
        emailInput.setHint("Enter your email");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailInput);

        builder.setView(layout)
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();

        dialog.show();

        Button submitButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        submitButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty()) {
                emailInput.setError("Email is required");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email address");
                return;
            }
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void showRegisterFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, new RegisterFragment())
                .commit();
    }

    private void showHomeFragment(String username) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, homeFragment)
                .commit();
    }

    private void initAppDatabase() {
        appDatabase = AppDatabase.getInstance(requireContext());
    }
}