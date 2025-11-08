package com.example.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private final LocalDateTime timestamp;
    private final String type;
    private final BigDecimal amount;
    private final BigDecimal resultingBalance;
    private final String description;

    public Transaction(LocalDateTime timestamp, String type, BigDecimal amount,
                       BigDecimal resultingBalance, String description) {
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
        this.type = Objects.requireNonNull(type, "type");
        this.amount = amount;
        this.resultingBalance = resultingBalance;
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getResultingBalance() {
        return resultingBalance;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", resultingBalance=" + resultingBalance +
                ", description='" + description + '\'' +
                '}';
    }
}
