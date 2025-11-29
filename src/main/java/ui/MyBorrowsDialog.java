package ui;

import model.Book;
import model.BorrowRecord;
import model.User;
import service.BookService;
import service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dialog showing active borrows for user.
 */
public class MyBorrowsDialog extends JDialog {
    private final User user;
    private final BorrowService borrowService;
    private final BookService bookService;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public MyBorrowsDialog(Frame owner, User user, BorrowService borrowService, BookService bookService) {
        super(owner, "My Borrows", true);
        this.user = user;
        this.borrowService = borrowService;
        this.bookService = bookService;
        setSize(600, 400);
        setLocationRelativeTo(owner);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Book Title", "Borrow Date", "Due Date", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton returnButton = new JButton("Return Selected");
        returnButton.addActionListener(e -> returnSelected());

        JPanel bottom = new JPanel();
        bottom.add(returnButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<BorrowRecord> records = borrowService.getActiveBorrows(user.getId());
        for (BorrowRecord br : records) {
            Book book = bookService.findById(br.getBookId());
            tableModel.addRow(new Object[]{br.getId(), book != null ? book.getTitle() : br.getBookId(), br.getBorrowDate(), br.getDueDate(), br.getStatus()});
        }
    }

    private void returnSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to return.");
            return;
        }
        int recordId = (int) tableModel.getValueAt(row, 0);
        List<BorrowRecord> records = borrowService.getActiveBorrows(user.getId());
        BorrowRecord target = records.stream().filter(r -> r.getId() == recordId).findFirst().orElse(null);
        if (target != null) {
            borrowService.returnBook(target);
            JOptionPane.showMessageDialog(this, "Book returned.");
            loadData();
        }
    }
}
