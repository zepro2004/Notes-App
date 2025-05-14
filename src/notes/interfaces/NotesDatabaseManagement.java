package notes.interfaces;

import notes.Notes;
import common.interfaces.DatabaseManagement;
import java.util.List;

public interface NotesDatabaseManagement extends DatabaseManagement<Notes> {
    List<Notes> findByTitle(String title);
}

