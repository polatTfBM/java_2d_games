package com.example.bank;

import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.service.Bank;
import com.example.bank.service.BankService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BankApplication {
    private final BankService bankService;
    private final Scanner scanner = new Scanner(System.in);

    public BankApplication(BankService bankService) {
        this.bankService = bankService;
    }

    public static void main(String[] args) {
        Bank bank = new Bank();
        BankService bankService = new BankService(bank);
        BankApplication application = new BankApplication(bankService);
        application.seedData();
        application.run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1" -> openAccount();
                    case "2" -> listAccounts();
                    case "3" -> deposit();
                    case "4" -> withdraw();
                    case "5" -> transfer();
                    case "6" -> showTransactions();
                    case "0" -> running = false;
                    default -> System.out.println("Geçersiz seçim. Tekrar deneyin.");
                }
            } catch (Exception e) {
                System.out.println("Hata: " + e.getMessage());
            }
        }
        System.out.println("Bankamızı kullandığınız için teşekkürler!");
    }

    private void printMenu() {
        System.out.println("\n==== Banka Sistemi ====");
        System.out.println("1. Hesap Aç");
        System.out.println("2. Hesapları Listele");
        System.out.println("3. Para Yatır");
        System.out.println("4. Para Çek");
        System.out.println("5. Havale/EFT");
        System.out.println("6. Hesap Hareketleri");
        System.out.println("0. Çıkış");
        System.out.print("Seçiminiz: ");
    }

    private void openAccount() {
        System.out.print("Müşteri adı: ");
        String ownerName = scanner.nextLine();
        System.out.print("Hesap türü (Vadesiz/Vadeli): ");
        String type = scanner.nextLine();
        Account account = bankService.openAccount(ownerName, type);
        System.out.println("Hesap açıldı. Hesap numarası: " + account.getAccountNumber());
    }

    private void listAccounts() {
        System.out.println("\nAktif Hesaplar:");
        List<Account> accounts = bankService.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("Henüz hesap yok.");
            return;
        }
        accounts.forEach(account -> System.out.printf("%s - %s (%s) Bakiye: %s TL%n",
                account.getAccountNumber(), account.getOwnerName(), account.getType(), account.getBalance()));
    }

    private void deposit() {
        System.out.print("Hesap numarası: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Tutar: ");
        BigDecimal amount = readAmount();
        System.out.print("Açıklama: ");
        String description = scanner.nextLine();
        bankService.deposit(accountNumber, amount, description);
        System.out.println("Para yatırma işlemi başarılı.");
    }

    private void withdraw() {
        System.out.print("Hesap numarası: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Tutar: ");
        BigDecimal amount = readAmount();
        System.out.print("Açıklama: ");
        String description = scanner.nextLine();
        bankService.withdraw(accountNumber, amount, description);
        System.out.println("Para çekme işlemi başarılı.");
    }

    private void transfer() {
        System.out.print("Gönderen hesap numarası: ");
        String fromAccount = scanner.nextLine();
        System.out.print("Alıcı hesap numarası: ");
        String toAccount = scanner.nextLine();
        System.out.print("Tutar: ");
        BigDecimal amount = readAmount();
        System.out.print("Açıklama: ");
        String description = scanner.nextLine();
        bankService.transfer(fromAccount, toAccount, amount, description);
        System.out.println("Transfer işlemi başarılı.");
    }

    private void showTransactions() {
        System.out.print("Hesap numarası: ");
        String accountNumber = scanner.nextLine();
        List<Transaction> transactions = bankService.listTransactions(accountNumber);
        if (transactions.isEmpty()) {
            System.out.println("Hesap hareketi bulunmuyor.");
            return;
        }
        System.out.println("\nHesap Hareketleri:");
        transactions.forEach(transaction -> System.out.printf("%s | %s | %s | %s | %s%n",
                transaction.getTimestamp(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getResultingBalance(),
                transaction.getDescription()));
    }

    private BigDecimal readAmount() {
        String input = scanner.nextLine();
        return new BigDecimal(input.replace(",", "."));
    }

    private void seedData() {
        Account ali = bankService.openAccount("Ali Veli", "Vadesiz");
        Account ayse = bankService.openAccount("Ayşe Yılmaz", "Vadeli");

        bankService.deposit(ali.getAccountNumber(), BigDecimal.valueOf(1500), "Maaş");
        bankService.deposit(ayse.getAccountNumber(), BigDecimal.valueOf(2000), "Birikim");
        bankService.transfer(ali.getAccountNumber(), ayse.getAccountNumber(), BigDecimal.valueOf(250), "Kira");
    }
}
