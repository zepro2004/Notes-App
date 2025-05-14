package layout.panels;

import common.interfaces.DatabaseManagement;
import common.interfaces.Services;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base panel for managing items (notes or todos).
 * Provides common functionality for both NotesPanel and ToDoPanel.
 *
 * @param <T> The type of item being managed
 */
public abstract class GeneralPanel<T> extends JPanel {
    protected final Services<T> service;
    protected final JPanel buttonPanel   = new JPanel();
    protected JButton saveButton = new JButton("Save");
    protected JButton deleteButton = new JButton("Delete");
    protected JButton editButton = new JButton("Edit");
    protected JComboBox<String> sortOptions;
    protected DefaultListModel<String> listModel = new DefaultListModel<>();
    protected JList<String> itemList = new JList<>(listModel);
    protected boolean isEditing = false;
    protected T editingItem = null;

    protected GeneralPanel(Services<T> service) {
        this.service = service;
        initializeComponents();
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(saveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
        setupListeners();
        displayItems();
    }

    private void setupListeners() {
        saveButton.addActionListener(e -> onSave());
        editButton.addActionListener(e -> onEdit());
        deleteButton.addActionListener(e -> onDelete());
        sortOptions.addActionListener(e -> onSort());
    }

    private void onSave() {
        if(!validateInput()) return;
        if(isEditing && editingItem != null) {
            updateExistingItem(editingItem);
        }  else {
            T item = createNewItem();
            service.add(item);
        }
        isEditing = false;
        editingItem = null;
        clearInputFields();
        displayItems();
        selectFirst();
    }

    private void onEdit() {
        int selected = itemList.getSelectedIndex();
        if (selected >= 0) {
            editingItem = service.getAll().get(selected);
            isEditing = true;
            populateInputFields(editingItem);
        }
        else {
            showError("Please select an item to edit.");
        }
    }

    private void onDelete() {
        int selected = itemList.getSelectedIndex();
        if (selected >= 0) {
            T item = service.getAll().get(selected);
            service.delete(item);
            displayItems();
        } else {
            showError("Please select an item to delete.");
        }
    }

    private void onSort() {
        service.sort();
        updateListModel();
    }

    protected void displayItems() {
        service.refresh();
        updateListModel();
    }

    private void updateListModel() {
        listModel.clear();
        for(String s: service.getSummary()) {
            listModel.addElement(s);
        }
    }

    protected void selectFirst() {
        if(!listModel.isEmpty()) {
            itemList.setSelectedIndex(0);
            itemList.ensureIndexIsVisible(0);
        }
    }

    protected void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected JComponent createCenterPanel() {
        return new JScrollPane(itemList);
    }

    protected abstract JPanel createInputPanel();
    protected abstract void clearInputFields();
    protected abstract boolean validateInput();
    protected abstract void populateInputFields(T item);
    protected abstract T createNewItem();
    protected abstract void updateExistingItem(T editingItem);
    protected abstract void initializeComponents();
}