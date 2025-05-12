package notes.impl;

import notes.Notes;
import common.interfaces.Services;
import notes.interfaces.NotesDatabaseManagement;
import java.util.ArrayList;
import java.util.List;

public class NotesService implements Services<Notes> {
    private final NotesDatabaseManagement repository;
    private List<Notes> notesList;

    public NotesService(NotesDatabaseManagement repository) {
        this.repository = repository;
        this.notesList = new ArrayList<>();
        refresh();
    }

    @Override
    public List<Notes> getAll() {
        return notesList;
    }

    @Override
    public void add(Notes note) {
        repository.save(note);
        notesList.add(note);
    }

    @Override
    public void delete(Notes note) {
        repository.delete(note);
        notesList.remove(note);
    }

    @Override
    public void refresh() {
        notesList = repository.refresh();
    }

    @Override
    public void update(Notes note) {
        repository.update(note);
        refresh();
    }

    @Override
    public void clear() {
        repository.clear();
        notesList.clear();
    }

    public List<String> getSummary() {
        List<String> summaries = new ArrayList<>();
        for (Notes note : notesList) {
            summaries.add(note.getTitle());
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

