package service;

import model.Book;
import repository.BookRepository;
import util.FileLogger;

import java.util.List;

/**
 * Business logic for books.
 */
public class BookService {
    private final BookRepository bookRepository;

    public BookService() {
        this.bookRepository = new BookRepository();
    }

    public void addBook(Book book) {
        bookRepository.createBook(book);
        FileLogger.log("Book added: " + book.getTitle());
    }

    public void updateBook(Book book) {
        bookRepository.updateBook(book);
        FileLogger.log("Book updated: " + book.getTitle());
    }

    public void deleteBook(int id) {
        bookRepository.deleteBook(id);
        FileLogger.log("Book deleted: " + id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllBooks();
        }
        return bookRepository.searchByTitleOrAuthor(keyword);
    }

    public Book findById(int id) {
        return bookRepository.findById(id);
    }
}
