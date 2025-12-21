package service;

import abstract_.BaseService;
import exception.BusinessException;
import exception.ValidationException;
import model.Book;
import model.Borrow;
import repository.BorrowRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BorrowService extends BaseService {

    private final BorrowRepository borrowRepository;

    public BorrowService() {
        this.borrowRepository = new BorrowRepository();
    }

    public Borrow borrowBook(Book book, int userId) {
        validateBeforeCreate(book);
        try {
            Optional<Borrow> active = borrowRepository.findActiveBorrow(userId, book.getId());
            if (active.isPresent()) {
                throw new ValidationException("You already borrowed this book");
            }
            if (book.getAvailableCopies() <= 0) {
                throw new ValidationException("No available copies to borrow");
            }
            Borrow borrow = new Borrow();
            borrow.setBookId(book.getId());
            borrow.setUserId(userId);
            borrow.setBorrowDate(LocalDateTime.now());
            borrow.setReturned(false);
            borrowRepository.save(borrow);
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            return borrow;
        } catch (SQLException e) {
            throw new BusinessException("Unable to borrow book");
        }
    }

    public Borrow returnBook(Book book, int userId) {
        validateBeforeUpdate(book);
        try {
            Optional<Borrow> active = borrowRepository.findActiveBorrow(userId, book.getId());
            if (!active.isPresent()) {
                throw new ValidationException("No active borrow found for this book");
            }
            Borrow borrow = active.get();
            borrow.setReturned(true);
            borrow.setReturnDate(LocalDateTime.now());
            borrowRepository.updateReturn(borrow);
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            return borrow;
        } catch (SQLException e) {
            throw new BusinessException("Unable to return book");
        }
    }

    public int countActiveBorrows(int bookId) {
        try {
            return borrowRepository.countActiveBorrowsForBook(bookId);
        } catch (SQLException e) {
            throw new BusinessException("Unable to count borrows");
        }
    }

    public List<Borrow> findByUser(int userId) {
        try {
            return borrowRepository.findByUser(userId);
        } catch (SQLException e) {
            throw new BusinessException("Unable to load borrows");
        }
    }

    @Override
    protected void validateBeforeCreate(Object value) {
        if (value == null) {
            throw new ValidationException("Book is required");
        }
    }

    @Override
    protected void validateBeforeUpdate(Object value) {
        validateBeforeCreate(value);
    }
}
