package com.example.bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * In-memory repository of accounts with simple operations.
 */
public class Bank {
    private final List<Account> accounts = new ArrayList<>();

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public Account createAccount(String ownerName, double initialBalance) {
        String accountNumber = String.format("ACC-%04d", accounts.size() + 1);
        Account account = new Account(accountNumber, ownerName, initialBalance);
        accounts.add(account);
        return account;
    }

    public Optional<Account> findAccount(String accountNumber) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    public void deposit(String accountNumber, double amount) {
        Account account = requireAccount(accountNumber);
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = requireAccount(accountNumber);
        account.withdraw(amount);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        Account fromAccount = requireAccount(fromAccountNumber);
        Account toAccount = requireAccount(toAccountNumber);
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
    }

    private Account requireAccount(String accountNumber) {
        return findAccount(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
    }
}
