package com.example.bank.service;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankService {
    private final Bank bank;

    public BankService(Bank bank) {
        this.bank = bank;
    }

    public Account openAccount(String ownerName, String type) {
        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, ownerName, type);
        bank.addAccount(account);
        recordTransaction(account, "ACCOUNT_OPENED", BigDecimal.ZERO, "Yeni hesap açıldı");
        return account;
    }

    public List<Account> listAccounts() {
        return bank.getAllAccounts().stream()
                .sorted(Comparator.comparing(Account::getOwnerName))
                .collect(Collectors.toList());
    }

    public List<Transaction> listTransactions(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);
        return account.getTransactions();
    }

    public void deposit(String accountNumber, BigDecimal amount, String description) {
        Account account = getAccountOrThrow(accountNumber);
        validateAmount(amount);
        BigDecimal newBalance = account.getBalance().add(amount);
        account.updateBalance(newBalance);
        recordTransaction(account, "DEPOSIT", amount, description);
    }

    public void withdraw(String accountNumber, BigDecimal amount, String description) {
        Account account = getAccountOrThrow(accountNumber);
        validateAmount(amount);
        ensureSufficientFunds(account, amount);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.updateBalance(newBalance);
        recordTransaction(account, "WITHDRAW", amount.negate(), description);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Hesap numaraları farklı olmalı");
        }
        Account from = getAccountOrThrow(fromAccountNumber);
        Account to = getAccountOrThrow(toAccountNumber);
        validateAmount(amount);
        ensureSufficientFunds(from, amount);

        BigDecimal fromNewBalance = from.getBalance().subtract(amount);
        BigDecimal toNewBalance = to.getBalance().add(amount);

        from.updateBalance(fromNewBalance);
        to.updateBalance(toNewBalance);

        recordTransaction(from, "TRANSFER_OUT", amount.negate(), description + " - gönderim");
        recordTransaction(to, "TRANSFER_IN", amount, description + " - alım");
    }

    private Account getAccountOrThrow(String accountNumber) {
        return bank.findAccount(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı: " + accountNumber));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tutar pozitif olmalı");
        }
    }

    private void ensureSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Yetersiz bakiye");
        }
    }

    private void recordTransaction(Account account, String type, BigDecimal amount, String description) {
        Transaction transaction = new Transaction(
                LocalDateTime.now(),
                type,
                amount.setScale(2, RoundingMode.HALF_EVEN),
                account.getBalance().setScale(2, RoundingMode.HALF_EVEN),
                description
        );
        account.addTransaction(transaction);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
