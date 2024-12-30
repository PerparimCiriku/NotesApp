package com.example.androidapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapters.NotesAdapter;
import com.example.androidapp.db.AppDatabase;
import com.example.androidapp.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNote;
    private NotesAdapter adapter;
    private List<Note> notes = new ArrayList<>();
    private AppDatabase appDatabase;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view);
        createNotificationChannel();
        initAppDatabase();
        findViewById(view);
        setupRecyclerView();
        if (getArguments() != null && getArguments().getString("username") != null) {
            String username = getArguments().getString("username");
            fetchUserIdAndLoadNotes(username);
            showWelcomeNotification(username);
        }
        onClick();
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_logout) {
                Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
                logout();
                return true;
            }
            return false;
        });
    }

    private void initAppDatabase() {
        appDatabase = AppDatabase.getInstance(requireContext());
    }

    private void findViewById(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        fabAddNote = view.findViewById(R.id.fabAddNote);
    }

    private void onClick() {
        fabAddNote.setOnClickListener(v -> showEditDialog(null));
    }

    private void setupRecyclerView() {
        adapter = new NotesAdapter(notes, new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onEdit(Note note) {
                showEditDialog(note);
            }

            @Override
            public void onDelete(Note note) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    appDatabase.noteDao().delete(note);
                    loadNotes();
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fetchUserIdAndLoadNotes(String username) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userId = appDatabase.userDao().getUserId(username);
            loadNotes();
        });
    }

    private void loadNotes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            notes.clear();
            notes.addAll(appDatabase.noteDao().getNotesByUser(userId));
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    private void showEditDialog(Note note) {
        boolean isEditing = note != null;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(isEditing ? "Edit Note" : "Add Note");

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit_note, null, false);
        builder.setView(dialogView);

        EditText etTitle = dialogView.findViewById(R.id.etNoteTitle);
        EditText etDescription = dialogView.findViewById(R.id.etNoteDescription);

        if (isEditing) {
            etTitle.setText(note.getTitle());
            etDescription.setText(note.getDescription());
        }

        builder.setPositiveButton(isEditing ? "Update" : "Add", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                if (isEditing) {
                    note.setTitle(title);
                    note.setDescription(description);
                    appDatabase.noteDao().update(note);
                } else {
                    Note newNote = new Note();
                    newNote.setUserId(userId);
                    newNote.setTitle(title);
                    newNote.setDescription(description);
                    newNote.setCreatedAt(System.currentTimeMillis());
                    appDatabase.noteDao().insert(newNote);
                }
                loadNotes();
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void logout() {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                )
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "login_notifications";
            String channelName = "Login Notifications";
            String channelDescription = "Notifications shown after login";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("MissingPermission")
    private void showWelcomeNotification(String username) {
        String channelId = "login_notifications";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Welcome!")
                .setContentText("Hello, " + username + "! Welcome to the app.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(1, builder.build());
    }
}

