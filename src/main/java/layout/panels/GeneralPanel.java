package layout.panels;

import common.interfaces.Services;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base panel that provides a generic implementation for managing items (notes or todos).
 * This panel implements the template method pattern to provide common CRUD functionality
 * while allowing subclasses to define specific input handling and validation.
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Standard CRUD operations (Create, Read, Update, Delete)</li>
 *   <li>Common UI layout with customizable input panel</li>
 *   <li>Integrated error handling and validation</li>
 *   <li>List-based item display with selection support</li>
 *   <li>Sort functionality</li>
 * </ul>
 *
 * <p>The panel uses a BorderLayout with:</p>
 * <ul>
 *   <li>NORTH: Input panel (defined by subclasses)</li>
 *   <li>CENTER: List of items (can be overridden)</li>
 *   <li>SOUTH: Button panel with standard operations</li>
 * </ul>
 *
 * <p>Implementation note: Subclasses must implement several abstract methods to define
 * item-specific behavior. This design follows the Template Method pattern where the base class
 * defines the algorithm structure while subclasses provide specific implementations.</p>
 *
 * @param <T> The type of item being managed (e.g., Note, ToDo), must be a model class
 *
 * @version 1.0
 * @since 2025-04-23
 * @author Louis Bertrand Ntwali
 * @see Services
 * @see JPanel
 * @see BorderLayout
 * @see layout.panels.ToDoPanel
 */
public abstract class GeneralPanel<T> extends JPanel {
    /** Service layer interface for CRUD operations on items */
    protected final Services<T> service;

    /** Panel containing action buttons (save, edit, delete) */
    protected final JPanel buttonPanel = new JPanel();

    /** Button to save new or updated items */
    protected JButton saveButton = new JButton("Save");

    /** Button to delete selected items */
    protected JButton deleteButton = new JButton("Delete");

    /** Button to edit selected items */
    protected JButton editButton = new JButton("Edit");

    /** Dropdown for sorting options */
    protected JComboBox<String> sortOptions;

    /** Model backing the item list */
    protected DefaultListModel<String> listModel = new DefaultListModel<>();

    /** List component displaying items */
    protected JList<String> itemList = new JList<>(listModel);

    /** Flag indicating if currently editing an item */
    protected boolean isEditing = false;

    /** Currently edited item, null if not editing */
    protected T editingItem = null;

    /**
     * Creates a new panel with the specified service.
     * Initializes components, sets up the layout, and attaches listeners.
     *
     * @param service The service implementation for managing items, must not be null
     * @throws IllegalArgumentException if service is null
     */
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

    /**
     * Sets up action listeners for the standard buttons.
     * Handles save, edit, and delete operations.
     */
    private void setupListeners() {
        saveButton.addActionListener(e -> onSave());
        editButton.addActionListener(e -> onEdit());
        deleteButton.addActionListener(e -> onDelete());
    }

    /**
     * Handles the save button action.
     * Validates input and either creates a new item or updates existing one.
     * If validation fails, the operation is aborted.
     */
    private void onSave() {
        if(!validateInput()) return;
        if(isEditing && editingItem != null) {
            updateExistingItem(editingItem);
        } else {
            T item = createNewItem();
            service.add(item);
        }
        isEditing = false;
        editingItem = null;
        clearInputFields();
        displayItems();
        selectFirst();
    }

    /**
     * Handles the edit button action.
     * Loads the selected item into the input fields for editing.
     * Displays an error message if no item is selected.
     */
    private void onEdit() {
        int selected = itemList.getSelectedIndex();
        if (selected >= 0) {
            editingItem = service.getAll().get(selected);
            isEditing = true;
            populateInputFields(editingItem);
        } else {
            showError("Please select an item to edit.");
        }
    }

    /**
     * Handles the delete button action.
     * Removes the selected item after confirmation.
     * Displays an error message if no item is selected.
     */
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

    /**
     * Refreshes the displayed items from the service layer.
     * Updates the list model with current data and redraws the list.
     */
    protected void displayItems() {
        service.refresh();
        updateListModel();
    }

    /**
     * Updates the list model with current items from the service.
     * Clears the existing model and populates it with summary strings.
     */
    void updateListModel() {
        listModel.clear();
        for(String s: service.getSummary()) {
            listModel.addElement(s);
        }
    }

    /**
     * Selects the first item in the list if available.
     * Does nothing if the list is empty.
     */
    protected void selectFirst() {
        if(!listModel.isEmpty()) {
            itemList.setSelectedIndex(0);
            itemList.ensureIndexIsVisible(0);
        }
    }

    /**
     * Displays an error message dialog.
     *
     * @param msg The error message to display, should be user-friendly
     */
    protected void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creates the center panel containing the item list.
     * Default implementation returns a scrollable list view.
     * Can be overridden to provide custom item display.
     *
     * @return Component to display in the center region, never null
     */
    protected JComponent createCenterPanel() {
        return new JScrollPane(itemList);
    }

    /**
     * Creates the input panel for item details.
     * Must be implemented by subclasses to provide type-specific inputs.
     *
     * @return Panel containing input components, never null
     */
    protected abstract JPanel createInputPanel();

    /**
     * Clears all input fields.
     * Must be implemented by subclasses to reset their specific inputs.
     * Called after a save operation or when canceling an edit.
     */
    protected abstract void clearInputFields();

    /**
     * Validates the current input values.
     * Must be implemented by subclasses to provide type-specific validation.
     * Implementation should display appropriate error messages for invalid input.
     *
     * @return true if input is valid, false otherwise
     */
    protected abstract boolean validateInput();

    /**
     * Populates input fields with values from an existing item.
     * Must be implemented by subclasses to handle their specific item type.
     * Called when editing an existing item.
     *
     * @param item The item whose values should populate the inputs, never null
     */
    protected abstract void populateInputFields(T item);

    /**
     * Creates a new item instance from current input values.
     * Must be implemented by subclasses to handle their specific item type.
     * Called when saving a new item.
     *
     * @return New instance of T with values from input fields, never null
     */
    protected abstract T createNewItem();

    /**
     * Updates an existing item with values from input fields.
     * Must be implemented by subclasses to handle their specific item type.
     * Called when saving changes to an existing item.
     *
     * @param editingItem The item to update, never null
     */
    protected abstract void updateExistingItem(T editingItem);

    /**
     * Initializes panel-specific components.
     * Must be implemented by subclasses to set up their unique UI elements.
     * Called during panel construction before layout is established.
     */
    protected abstract void initializeComponents();
}