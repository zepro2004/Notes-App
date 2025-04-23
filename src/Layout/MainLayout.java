package Layout;

import Layout.Panels.*;
import Notes.NotesManager;
import ToDo.ToDoManager;
import javax.swing.*;

public class MainLayout {
    private NotesManager notesManager;
    private ToDoManager toDoManager;
    private JFrame mainFrame;

    public MainLayout() {
        initializeManagers();
        setupMainFrame();
    }

    private void initializeManagers() {
        notesManager = new NotesManager();
        toDoManager = new ToDoManager();
    }

    private void setupMainFrame() {
        mainFrame = new JFrame("ToDo List And Notes App");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Notes", new NotesPanel(notesManager));
        tabbedPane.addTab("To Do List", new ToDoPanel(toDoManager));

        mainFrame.add(tabbedPane);
        mainFrame.setVisible(true);
    }
}