package ui;

import model.Book;
import model.BorrowRecord;
import model.User;
import service.BookService;
import service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Main application window after login.
 */
public class MainFrame extends JFrame {
    private final User user;
    private final BookService bookService;
    private final BorrowService borrowService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;

    public MainFrame(User user) {
        this.user = user;
        this.bookService = new BookService();
        this.borrowService = new BorrowService();

        setTitle("Library - Welcome " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        JPanel searchButtons = new JPanel();
        searchButtons.add(searchButton);
        searchButtons.add(refreshButton);
        searchPanel.add(searchButtons, BorderLayout.EAST);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Year", "Available"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton borrowButton = new JButton("Borrow");
        JButton myBorrowsButton = new JButton("My Borrows");
        JButton historyButton = new JButton("History");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(borrowButton);
        bottomPanel.add(myBorrowsButton);
        bottomPanel.add(historyButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> doSearch());
        refreshButton.addActionListener(e -> loadBooks());
        borrowButton.addActionListener(e -> borrowSelected());
        myBorrowsButton.addActionListener(e -> openMyBorrows());
        historyButton.addActionListener(e -> openHistory());

        loadBooks();
    }

    private void doSearch() {
        String keyword = searchField.getText().trim();
        List<Book> books = bookService.search(keyword);
        fillTable(books);
    }

    private void loadBooks() {
        List<Book> books = bookService.getAllBooks();
        fillTable(books);
    }

    private void fillTable(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getPublishYear(), b.getAvailableCopies()});
        }
    }

    private void borrowSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a book first.");
            return;
        }
        int bookId = (int) tableModel.getValueAt(row, 0);
        Book book = bookService.findById(bookId);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }
        String due = JOptionPane.showInputDialog(this, "Enter due date (YYYY-MM-DD)", LocalDate.now().plusDays(14).toString());
        if (due == null || due.isBlank()) {
            return;
        }
        try {
            LocalDate dueDate = LocalDate.parse(due.trim());
            boolean ok = borrowService.borrowBook(user, book, dueDate);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Book borrowed successfully.");
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "No available copies.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format.");
        }
    }

    private void openMyBorrows() {
        MyBorrowsDialog dialog = new MyBorrowsDialog(this, user, borrowService, bookService);
        dialog.setVisible(true);
        loadBooks();
    }

    private void openHistory() {
        HistoryDialog dialog = new HistoryDialog(this, user, borrowService);
        dialog.setVisible(true);
    }
}
