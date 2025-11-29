package ui;

import service.AuthService;

import javax.swing.*;
import java.awt.*;

/**
 * Registration window.
 */
public class RegisterFrame extends JFrame {
    private final JTextField usernameField;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmField;
    private final AuthService authService;
    private final JFrame parent;

    public RegisterFrame(JFrame parent, AuthService authService) {
        this.parent = parent;
        this.authService = authService;
        setTitle("Register");
        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        panel.add(confirmField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(registerButton, gbc);

        add(panel);

        registerButton.addActionListener(e -> doRegister());
    }

    private void doRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }
        boolean ok = authService.register(username, email, password);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Registration successful. You can login now.");
            dispose();
            if (parent != null) {
                parent.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        }
    }
}
