package com.example.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Account {
    private final String accountNumber;
    private final String ownerName;
    private final String type;
    private BigDecimal balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String ownerName, String type) {
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber");
        this.ownerName = Objects.requireNonNull(ownerName, "ownerName");
        this.type = Objects.requireNonNull(type, "type");
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void updateBalance(BigDecimal newBalance) {
        this.balance = newBalance.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
