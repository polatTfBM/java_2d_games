package interface_;

import model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book) throws SQLException;

    Book update(Book book) throws SQLException;

    void delete(int id) throws SQLException;

    Optional<Book> findById(int id) throws SQLException;

    List<Book> findAll() throws SQLException;

    List<Book> search(String title, String author, String category) throws SQLException;
}
