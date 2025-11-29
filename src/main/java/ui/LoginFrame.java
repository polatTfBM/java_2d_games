package ui;

import model.User;
import service.AuthService;

import javax.swing.*;
import java.awt.*;

/**
 * Login window.
 */
public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final AuthService authService;

    public LoginFrame() {
        this.authService = new AuthService();
        setTitle("Library Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> doLogin());
        registerButton.addActionListener(e -> openRegister());
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        User user = authService.login(username, password);
        if (user != null) {
            dispose();
            MainFrame mainFrame = new MainFrame(user);
            mainFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        }
    }

    private void openRegister() {
        RegisterFrame registerFrame = new RegisterFrame(this, authService);
        registerFrame.setVisible(true);
    }
}
