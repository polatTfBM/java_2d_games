package service;

import abstract_.BaseService;
import exception.BusinessException;
import exception.ValidationException;
import interface_.BookRepository;
import model.Book;
import repository.BookRepositoryImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookService extends BaseService {

    private final BookRepository bookRepository;
    private final BorrowService borrowService;

    public BookService(BorrowService borrowService) {
        this.bookRepository = new BookRepositoryImpl();
        this.borrowService = borrowService;
    }

    public Book create(Book book) {
        validateBeforeCreate(book);
        try {
            book.setAvailableCopies(book.getTotalCopies());
            return bookRepository.save(book);
        } catch (SQLException e) {
            throw new BusinessException("Unable to save book");
        }
    }

    public Book update(Book book) {
        validateBeforeUpdate(book);
        try {
            int activeBorrow = borrowService.countActiveBorrows(book.getId());
            if (book.getTotalCopies() < activeBorrow) {
                throw new ValidationException("Total copies cannot be less than borrowed count");
            }
            int calculatedAvailable = book.getTotalCopies() - activeBorrow;
            book.setAvailableCopies(calculatedAvailable);
            return bookRepository.update(book);
        } catch (SQLException e) {
            throw new BusinessException("Unable to update book");
        }
    }

    public void delete(int id) {
        try {
            int activeBorrow = borrowService.countActiveBorrows(id);
            if (activeBorrow > 0) {
                throw new ValidationException("Book cannot be deleted while borrowed");
            }
            bookRepository.delete(id);
        } catch (SQLException e) {
            throw new BusinessException("Unable to delete book");
        }
    }

    public List<Book> listAll() {
        try {
            return bookRepository.findAll();
        } catch (SQLException e) {
            throw new BusinessException("Unable to list books");
        }
    }

    public List<Book> search(String title, String author, String category) {
        try {
            return bookRepository.search(title, author, category);
        } catch (SQLException e) {
            throw new BusinessException("Unable to search books");
        }
    }

    public Optional<Book> findById(int id) {
        try {
            return bookRepository.findById(id);
        } catch (SQLException e) {
            throw new BusinessException("Unable to find book");
        }
    }

    @Override
    protected void validateBeforeCreate(Object value) {
        Book book = (Book) value;
        ensureStringNotEmpty(book.getTitle(), "Title is required");
        ensureStringNotEmpty(book.getAuthor(), "Author is required");
        ensureStringNotEmpty(book.getCategory(), "Category is required");
        if (book.getTotalCopies() < 0) {
            throw new ValidationException("Total copies cannot be negative");
        }
    }

    @Override
    protected void validateBeforeUpdate(Object value) {
        validateBeforeCreate(value);
    }
}
