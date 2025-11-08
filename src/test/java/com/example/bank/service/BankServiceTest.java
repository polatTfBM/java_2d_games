package com.example.bank.service;

import com.example.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {

    private BankService bankService;

    @BeforeEach
    void setUp() {
        bankService = new BankService(new Bank());
    }

    @Test
    void depositShouldIncreaseBalance() {
        Account account = bankService.openAccount("Test Kullanıcısı", "Vadesiz");

        bankService.deposit(account.getAccountNumber(), BigDecimal.valueOf(100), "Test");

        assertEquals(new BigDecimal("100.00"), account.getBalance());
        assertEquals(2, bankService.listTransactions(account.getAccountNumber()).size());
    }

    @Test
    void withdrawShouldDecreaseBalance() {
        Account account = bankService.openAccount("Test Kullanıcısı", "Vadesiz");
        bankService.deposit(account.getAccountNumber(), BigDecimal.valueOf(200), "Yatırma");

        bankService.withdraw(account.getAccountNumber(), BigDecimal.valueOf(75), "Fatura");

        assertEquals(new BigDecimal("125.00"), account.getBalance());
    }

    @Test
    void transferShouldMoveFundsBetweenAccounts() {
        Account from = bankService.openAccount("Gönderen", "Vadesiz");
        Account to = bankService.openAccount("Alıcı", "Vadesiz");
        bankService.deposit(from.getAccountNumber(), BigDecimal.valueOf(500), "Yatırma");

        bankService.transfer(from.getAccountNumber(), to.getAccountNumber(), BigDecimal.valueOf(120), "Kira");

        assertEquals(new BigDecimal("380.00"), from.getBalance());
        assertEquals(new BigDecimal("120.00"), to.getBalance());
    }

    @Test
    void withdrawShouldFailWhenInsufficientBalance() {
        Account account = bankService.openAccount("Test", "Vadesiz");
        bankService.deposit(account.getAccountNumber(), BigDecimal.valueOf(50), "Yatırma");

        assertThrows(IllegalArgumentException.class, () ->
                bankService.withdraw(account.getAccountNumber(), BigDecimal.valueOf(100), "Çekim"));
    }
}
