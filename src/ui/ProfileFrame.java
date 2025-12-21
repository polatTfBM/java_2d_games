package ui;

import abstract_.AbstractFrame;
import exception.ValidationException;
import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class ProfileFrame extends AbstractFrame {

    private final User user;
    private final UserService userService;

    private JTextField usernameField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;

    public ProfileFrame(User user, UserService userService) {
        super("Profile");
        this.user = user;
        this.userService = userService;
        buildLayout();
        bindEvents();
        center();
    }

    @Override
    protected void buildLayout() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Username"));
        usernameField = new JTextField(user.getUsername());
        panel.add(usernameField);
        panel.add(new JLabel("Old password"));
        oldPasswordField = new JPasswordField();
        panel.add(oldPasswordField);
        panel.add(new JLabel("New password"));
        newPasswordField = new JPasswordField();
        panel.add(newPasswordField);

        JButton saveButton = new JButton("Save");
        panel.add(saveButton);

        add(panel);

        saveButton.addActionListener(e -> saveProfile());
    }

    @Override
    protected void bindEvents() {
    }

    private void saveProfile() {
        try {
            User updated = userService.updateProfile(user.getId(), usernameField.getText(), new String(oldPasswordField.getPassword()), new String(newPasswordField.getPassword()));
            user.setUsername(updated.getUsername());
            user.setPassword(updated.getPassword());
            showMessage("Profile updated");
            dispose();
        } catch (ValidationException ex) {
            showMessage(ex.getMessage());
        } catch (Exception ex) {
            showMessage("Unable to update profile");
        }
    }
}
