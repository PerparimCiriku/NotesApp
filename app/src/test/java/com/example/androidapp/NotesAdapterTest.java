
package com.example.androidapp;

import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.example.androidapp.adapters.NotesAdapter;
import com.example.androidapp.models.Note;

import java.util.Arrays;

public class NotesAdapterTest {

    @Mock
    NotesAdapter.OnNoteClickListener listener;
    @Mock
    View mockView;
    @Mock
    NotesAdapter.NoteViewHolder holder;
    @Mock
    Note mockNote;

    private NotesAdapter adapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        adapter = new NotesAdapter(Arrays.asList(mockNote), listener);
    }

    @Test
    public void testOnNoteClickListener_Edit() {
        adapter.onBindViewHolder(holder, 0);
        holder.itemView.performClick();


        verify(listener).onEdit(mockNote);
    }

    @Test
    public void testOnNoteClickListener_Delete() {
        adapter.onBindViewHolder(holder, 0);
        holder.deleteButton.performClick();


        verify(listener).onDelete(mockNote);
    }
}

