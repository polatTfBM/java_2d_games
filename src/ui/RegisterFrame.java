package ui;

import abstract_.AbstractFrame;
import exception.ValidationException;
import service.BookService;
import service.BorrowService;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends AbstractFrame {

    private final UserService userService;
    private final BookService bookService;
    private final BorrowService borrowService;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame(UserService userService, BookService bookService, BorrowService borrowService) {
        super("Register");
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

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to login");
        panel.add(registerButton);
        panel.add(backButton);
        add(panel);

        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame(userService, bookService, borrowService).setVisible(true);
        });
    }

    @Override
    protected void bindEvents() {
    }

    private void handleRegister() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            userService.register(username, password);
            showMessage("Registration successful. You can login now.");
            dispose();
            new LoginFrame(userService, bookService, borrowService).setVisible(true);
        } catch (ValidationException ex) {
            showMessage(ex.getMessage());
        } catch (Exception ex) {
            showMessage("Registration failed");
        }
    }
}
