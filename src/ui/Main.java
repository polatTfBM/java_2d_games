package ui;

import db.DatabaseInitializer;
import service.BookService;
import service.BorrowService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        try {
            new DatabaseInitializer().initialize();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        BorrowService borrowService = new BorrowService();
        BookService bookService = new BookService(borrowService);
        UserService userService = new UserService();
        javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame(userService, bookService, borrowService).setVisible(true));
    }
}
