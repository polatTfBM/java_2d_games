package ui;

import abstract_.AbstractFrame;
import exception.ValidationException;
import model.Book;
import model.Role;
import model.User;
import service.BookService;
import service.BorrowService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookListFrame extends AbstractFrame {

    private final User currentUser;
    private final UserService userService;
    private final BookService bookService;
    private final BorrowService borrowService;

    private JTable bookTable;
    private JTextField titleSearch;
    private JTextField authorSearch;
    private JTextField categoryFilter;

    public BookListFrame(User user, UserService userService, BookService bookService, BorrowService borrowService) {
        super("Library Books");
        this.currentUser = user;
        this.userService = userService;
        this.bookService = bookService;
        this.borrowService = borrowService;
        buildLayout();
        bindEvents();
        loadBooks();
        center();
    }

    @Override
    protected void buildLayout() {
        setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        titleSearch = new JTextField();
        authorSearch = new JTextField();
        categoryFilter = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search and filter"));
        searchPanel.add(new JLabel("Title"));
        searchPanel.add(titleSearch);
        searchPanel.add(new JLabel("Author"));
        searchPanel.add(authorSearch);
        searchPanel.add(new JLabel("Category"));
        searchPanel.add(categoryFilter);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);

        JPanel actionPanel = new JPanel();
        JButton borrowButton = new JButton("Borrow");
        JButton returnButton = new JButton("Return");
        JButton profileButton = new JButton("Profile");
        JButton logoutButton = new JButton("Logout");
        actionPanel.add(borrowButton);
        actionPanel.add(returnButton);
        if (currentUser.getRole() == Role.ADMIN) {
            JButton addButton = new JButton("Add");
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            JButton usersButton = new JButton("List Users");
            actionPanel.add(addButton);
            actionPanel.add(editButton);
            actionPanel.add(deleteButton);
            actionPanel.add(usersButton);

            addButton.addActionListener(e -> openBookForm(null));
            editButton.addActionListener(e -> {
                Book book = getSelectedBook();
                if (book != null) {
                    openBookForm(book);
                }
            });
            deleteButton.addActionListener(e -> deleteBook());
            usersButton.addActionListener(e -> showUsers());
        }
        actionPanel.add(profileButton);
        actionPanel.add(logoutButton);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> loadBooks());
        clearButton.addActionListener(e -> {
            titleSearch.setText("");
            authorSearch.setText("");
            categoryFilter.setText("");
            loadBooks();
        });

        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        profileButton.addActionListener(e -> new ProfileFrame(currentUser, userService).setVisible(true));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame(userService, bookService, borrowService).setVisible(true);
        });
    }

    @Override
    protected void bindEvents() {
    }

    private void loadBooks() {
        List<Book> books = bookService.search(titleSearch.getText(), authorSearch.getText(), categoryFilter.getText());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Id", "Title", "Author", "Category", "Available", "Total"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Book book : books) {
            model.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getAvailableCopies(), book.getTotalCopies()});
        }
        bookTable.setModel(model);
    }

    private Book getSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Select a book first");
            return null;
        }
        int id = (int) bookTable.getValueAt(selectedRow, 0);
        return bookService.findById(id).orElse(null);
    }

    private void borrowBook() {
        Book book = getSelectedBook();
        if (book == null) {
            return;
        }
        try {
            borrowService.borrowBook(book, currentUser.getId());
            bookService.update(book);
            loadBooks();
            showMessage("Book borrowed");
        } catch (ValidationException ex) {
            showMessage(ex.getMessage());
        } catch (Exception ex) {
            showMessage("Borrow failed");
        }
    }

    private void returnBook() {
        Book book = getSelectedBook();
        if (book == null) {
            return;
        }
        try {
            borrowService.returnBook(book, currentUser.getId());
            bookService.update(book);
            loadBooks();
            showMessage("Book returned");
        } catch (ValidationException ex) {
            showMessage(ex.getMessage());
        } catch (Exception ex) {
            showMessage("Return failed");
        }
    }

    private void openBookForm(Book book) {
        BookFormFrame frame = new BookFormFrame(bookService, book, borrowService);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadBooks();
            }
        });
        frame.setVisible(true);
    }

    private void deleteBook() {
        Book book = getSelectedBook();
        if (book == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookService.delete(book.getId());
                loadBooks();
            } catch (ValidationException ex) {
                showMessage(ex.getMessage());
            } catch (Exception ex) {
                showMessage("Delete failed");
            }
        }
    }

    private void showUsers() {
        StringBuilder builder = new StringBuilder();
        userService.listUsers().forEach(u -> builder.append(u.getId()).append(" - ").append(u.getUsername()).append(" (" + u.getRole() + ")\n"));
        JTextArea area = new JTextArea(builder.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Users", JOptionPane.INFORMATION_MESSAGE);
    }
}
