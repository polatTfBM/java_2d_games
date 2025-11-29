package service;

import model.Book;
import model.BorrowRecord;
import model.User;
import repository.BookRepository;
import repository.BorrowRecordRepository;
import repository.UserHistoryRepository;
import util.FileLogger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles borrow and return operations.
 */
public class BorrowService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserHistoryRepository userHistoryRepository;

    public BorrowService() {
        this.borrowRecordRepository = new BorrowRecordRepository();
        this.bookRepository = new BookRepository();
        this.userHistoryRepository = new UserHistoryRepository();
    }

    public boolean borrowBook(User user, Book book, LocalDate dueDate) {
        if (book.getAvailableCopies() <= 0) {
            return false;
        }
        BorrowRecord record = new BorrowRecord();
        record.setUserId(user.getId());
        record.setBookId(book.getId());
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(dueDate.atStartOfDay());
        record.setStatus(BorrowRecord.STATUS_BORROWED);
        borrowRecordRepository.createBorrowRecord(record);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.updateBook(book);

        String action = "BORROWED: " + book.getTitle();
        userHistoryRepository.logAction(user.getId(), action);
        FileLogger.log("User " + user.getUsername() + " borrowed book: " + book.getTitle());
        return true;
    }

    public void returnBook(BorrowRecord record) {
        Book book = bookRepository.findById(record.getBookId());
        if (book == null) {
            return;
        }
        record.setReturnDate(LocalDateTime.now());
        record.setStatus(BorrowRecord.STATUS_RETURNED);
        borrowRecordRepository.updateBorrowRecord(record);

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.updateBook(book);

        userHistoryRepository.logAction(record.getUserId(), "RETURNED: " + book.getTitle());
        FileLogger.log("Book returned: " + book.getTitle());
    }

    public List<BorrowRecord> getActiveBorrows(int userId) {
        return borrowRecordRepository.findActiveBorrowsByUser(userId);
    }

    public List<BorrowRecord> getHistory(int userId) {
        return borrowRecordRepository.findHistoryByUser(userId);
    }
}
