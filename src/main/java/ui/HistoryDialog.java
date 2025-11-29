package ui;

import model.BorrowRecord;
import model.User;
import service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Dialog to display user borrow history.
 */
public class HistoryDialog extends JDialog {
    private final BorrowService borrowService;
    private final User user;
    private final DefaultTableModel tableModel;

    public HistoryDialog(Frame owner, User user, BorrowService borrowService) {
        super(owner, "Borrow History", true);
        this.user = user;
        this.borrowService = borrowService;
        setSize(600, 400);
        setLocationRelativeTo(owner);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Book", "Borrow Date", "Due Date", "Return Date", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<BorrowRecord> records = borrowService.getHistory(user.getId());
        for (BorrowRecord br : records) {
            tableModel.addRow(new Object[]{br.getId(), br.getBookId(), br.getBorrowDate(), br.getDueDate(), br.getReturnDate(), br.getStatus()});
        }
    }
}
