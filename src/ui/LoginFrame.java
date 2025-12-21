package ui;

import abstract_.AbstractFrame;
import exception.ValidationException;
import model.User;
import service.BookService;
import service.BorrowService;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends AbstractFrame {

    private final UserService userService;
    private final BookService bookService;
    private final BorrowService borrowService;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(UserService userService, BookService bookService, BorrowService borrowService) {
        super("Library Login");
        this.userService = userService;
        this.bookService = bookService;
        this.borrowService = borrowService;
        buildLayout();
        bindEvents();
        center();
    }

    @Override
    protected void buildLayout() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame(userService, bookService, borrowService).setVisible(true);
        });
    }

    @Override
    protected void bindEvents() {
    }

    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = userService.login(username, password);
            dispose();
            new BookListFrame(user, userService, bookService, borrowService).setVisible(true);
        } catch (ValidationException ex) {
            showMessage(ex.getMessage());
        } catch (Exception ex) {
            showMessage("Login failed");
        }
    }
}
