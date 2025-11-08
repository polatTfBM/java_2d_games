package com.example.bank.ui;

import com.example.bank.model.Account;
import com.example.bank.model.Bank;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

/**
 * Simple Swing based banking dashboard.
 */
public class BankApp extends JFrame {
    private final Bank bank = new Bank();
    private final DefaultListModel<Account> accountListModel = new DefaultListModel<>();
    private final JList<Account> accountJList = new JList<>(accountListModel);
    private final JTextField ownerField = new JTextField();
    private final JTextField initialBalanceField = new JTextField("0");
    private final JTextField amountField = new JTextField();
    private final JTextField targetAccountField = new JTextField();
    private final JLabel statusLabel = new JLabel("Hoş geldiniz!");

    public BankApp() {
        super("Görselli Banka Sistemi");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        add(createListPanel(), BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.EAST);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        accountJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountJList.setCellRenderer(new AccountRenderer());
        panel.add(new JLabel("Hesaplar"), BorderLayout.NORTH);
        panel.add(new JScrollPane(accountJList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(sectionLabel("Yeni Hesap Oluştur"));
        panel.add(labeledField("Hesap Sahibi", ownerField));
        panel.add(labeledField("Başlangıç Bakiyesi", initialBalanceField));
        panel.add(actionButton("Hesap Ekle", e -> createAccount()));
        panel.add(Box.createVerticalStrut(20));

        panel.add(sectionLabel("İşlem"));
        panel.add(labeledField("Tutar", amountField));
        panel.add(labeledField("Hedef Hesap (Havale)", targetAccountField));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 5, 5));
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttons.add(actionButton("Para Yatır", e -> deposit()));
        buttons.add(actionButton("Para Çek", e -> withdraw()));
        buttons.add(actionButton("Havale", e -> transfer()));
        panel.add(buttons);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
        statusLabel.setForeground(new Color(0x2c3e50));
        statusPanel.add(statusLabel);
        return statusPanel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        return label;
    }

    private JPanel labeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(label);
        panel.add(field);
        panel.add(Box.createVerticalStrut(5));
        return panel;
    }

    private JButton actionButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void createAccount() {
        String owner = ownerField.getText().trim();
        double initialBalance;
        try {
            initialBalance = Double.parseDouble(initialBalanceField.getText().trim());
            if (initialBalance < 0) {
                throw new NumberFormatException("negative");
            }
        } catch (NumberFormatException ex) {
            showError("Geçersiz başlangıç bakiyesi");
            return;
        }

        try {
            Account account = bank.createAccount(owner, initialBalance);
            accountListModel.addElement(account);
            ownerField.setText("");
            initialBalanceField.setText("0");
            showSuccess(String.format("%s için hesap oluşturuldu", account.getOwnerName()));
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void deposit() {
        Optional<Account> selected = selectedAccount();
        if (selected.isEmpty()) {
            showError("Lütfen bir hesap seçin");
            return;
        }

        double amount = parseAmount();
        if (amount <= 0) {
            return;
        }

        try {
            bank.deposit(selected.get().getAccountNumber(), amount);
            refreshAccounts();
            showSuccess(String.format("%s hesabına %.2f ₺ yatırıldı", selected.get().getOwnerName(), amount));
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void withdraw() {
        Optional<Account> selected = selectedAccount();
        if (selected.isEmpty()) {
            showError("Lütfen bir hesap seçin");
            return;
        }

        double amount = parseAmount();
        if (amount <= 0) {
            return;
        }

        try {
            bank.withdraw(selected.get().getAccountNumber(), amount);
            refreshAccounts();
            showSuccess(String.format("%s hesabından %.2f ₺ çekildi", selected.get().getOwnerName(), amount));
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void transfer() {
        Optional<Account> selected = selectedAccount();
        if (selected.isEmpty()) {
            showError("Lütfen gönderen hesabı seçin");
            return;
        }

        String target = targetAccountField.getText().trim();
        if (target.isEmpty()) {
            showError("Hedef hesap numarasını girin");
            return;
        }

        double amount = parseAmount();
        if (amount <= 0) {
            return;
        }

        try {
            bank.transfer(selected.get().getAccountNumber(), target, amount);
            refreshAccounts();
            showSuccess(String.format("%s hesabından %s hesabına %.2f ₺ gönderildi",
                    selected.get().getAccountNumber(), target, amount));
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshAccounts() {
        accountListModel.clear();
        bank.getAccounts().forEach(accountListModel::addElement);
    }

    private double parseAmount() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                showError("Tutar pozitif olmalı");
                return -1;
            }
            return amount;
        } catch (NumberFormatException ex) {
            showError("Geçersiz tutar");
            return -1;
        }
    }

    private Optional<Account> selectedAccount() {
        return Optional.ofNullable(accountJList.getSelectedValue());
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(0x1e8449));
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(0xc0392b));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankApp app = new BankApp();
            app.setVisible(true);
        });
    }

    private static class AccountRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Account account) {
                setText(account.toString());
            }
            return component;
        }
    }
}
