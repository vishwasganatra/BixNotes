package com.vbix.vishwas.notes.listeners;

import com.vbix.vishwas.notes.entities.Note;

public interface NotesListeners {
    void onNoteClicked(Note note,int position);
}
