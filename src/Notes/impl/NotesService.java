package Notes.impl;

import Notes.Notes;
import Notes.interfaces.NotesDatabaseManagement;
import java.util.ArrayList;
import java.util.List;

public class NotesService {
    private final NotesDatabaseManagement repository;
    private List<Notes> notesList;

    public NotesService(NotesDatabaseManagement repository) {
        this.repository = repository;
        this.notesList = new ArrayList<>();
        refreshNotes();
    }

    public List<Notes> getNotes() {
        return notesList;
    }

    public void addNote(String title, String content) {
        Notes note = new Notes(title, content);
        repository.save(note);
        notesList.add(note);
    }

    public void deleteNote(Notes note) {
        repository.delete(note);
        notesList.remove(note);
    }

    public void refreshNotes() {
        notesList = repository.refresh();
    }

    public void updateNote(Notes note) {
        repository.update(note);
        refreshNotes();
    }

    public void clearNotes() {
        repository.clear();
        notesList.clear();
    }

    public List<String> getNotesSummary() {
        List<String> summaries = new ArrayList<>();
        for (Notes note : notesList) {
            summaries.add(note.getTitle() + " - Content: " + note.getContent());
        }
        return summaries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Notes note : notesList) {
            sb.append(note.getTitle()).append(" (").append(note.getContent()).append(")\n");
        }
        return sb.toString();
    }
}

