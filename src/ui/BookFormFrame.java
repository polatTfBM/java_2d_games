package ui;

import abstract_.AbstractFrame;
import exception.ValidationException;
import model.Book;
import service.BookService;
import service.BorrowService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class BookFormFrame extends AbstractFrame {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final Book book;

    private JTextField titleField;
    private JTextField authorField;
    private JTextField categoryField;
    private JTextArea descriptionArea;
    private JSpinner totalCopiesSpinner;
    private byte[] imageData;

    public BookFormFrame(BookService bookService, Book book, BorrowService borrowService) {
        super(book == null ? "Add Book" : "Edit Book");
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.book = book;
        buildLayout();
        bindEvents();
        center();
    }

    @Override
    protected void buildLayout() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titleField = new JTextField(book != null ? book.getTitle() : "");
        authorField = new JTextField(book != null ? book.getAuthor() : "");
        categoryField = new JTextField(book != null ? book.getCategory() : "");
        descriptionArea = new JTextArea(book != null ? book.getDescription() : "");
        totalCopiesSpinner = new JSpinner(new SpinnerNumberModel(book != null ? book.getTotalCopies() : 1, 1, 1000, 1));

        JButton imageButton = new JButton("Load image");
        imageButton.addActionListener(e -> selectImage());

        form.add(new JLabel("Title"));
        form.add(titleField);
        form.add(new JLabel("Author"));
        form.add(authorField);
        form.add(new JLabel("Category"));
        form.add(categoryField);
        form.add(new JLabel("Description"));
        form.add(new JScrollPane(descriptionArea));
        form.add(new JLabel("Total copies"));
        form.add(totalCopiesSpinner);

        add(form, BorderLayout.CENTER);
        add(imageButton, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        add(saveButton, BorderLayout.SOUTH);
        saveButton.addActionListener(e -> save());
    }

    @Override
    protected void bindEvents() {
    }

    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                imageData = Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                showMessage("Unable to read image");
            }
        }
    }

    private void save() {
        try {
            Book current = book != null ? book : new Book();
            current.setTitle(titleField.getText());
            current.setAuthor(authorField.getText());
            current.setCategory(categoryField.getText());
            current.setDescription(descriptionArea.getText());
            current.setTotalCopies((Integer) totalCopiesSpinner.getValue());
            if (imageData != null) {
                current.setBookImage(imageData);
            }
            if (book == null) {
                bookService.create(current);
            } else {
                int borrowedCount = borrowService.countActiveBorrows(current.getId());
                if (current.getTotalCopies() < borrowedCount) {
                    throw new ValidationException("Total copies cannot be less than borrowed count");
                }
                bookService.update(current);
            }
            showMessage("Saved successfully");
            dispose();
        } catch (ValidationException ex) {
            showMessage(ex.getMessage());
        } catch (Exception ex) {
            showMessage("Save failed");
        }
    }
}
