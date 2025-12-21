package repository;

import db.DatabaseConnection;
import interface_.BookRepository;
import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {

    @Override
    public Book save(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, category, description, totalCopies, availableCopies, bookImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(book, statement);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                book.setId(keys.getInt(1));
            }
            return book;
        }
    }

    @Override
    public Book update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, description = ?, totalCopies = ?, availableCopies = ?, bookImage = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(book, statement);
            statement.setInt(8, book.getId());
            statement.executeUpdate();
            return book;
        }
    }

    private void fillStatement(Book book, PreparedStatement statement) throws SQLException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getCategory());
        statement.setString(4, book.getDescription());
        statement.setInt(5, book.getTotalCopies());
        statement.setInt(6, book.getAvailableCopies());
        if (book.getBookImage() != null) {
            statement.setBytes(7, book.getBookImage());
        } else {
            statement.setNull(7, Types.BLOB);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() throws SQLException {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    @Override
    public List<Book> search(String title, String author, String category) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (title != null && !title.isEmpty()) {
            sql.append(" AND LOWER(title) LIKE ?");
            params.add("%" + title.toLowerCase() + "%");
        }
        if (author != null && !author.isEmpty()) {
            sql.append(" AND LOWER(author) LIKE ?");
            params.add("%" + author.toLowerCase() + "%");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND LOWER(category) = ?");
            params.add(category.toLowerCase());
        }

        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setCategory(rs.getString("category"));
        book.setDescription(rs.getString("description"));
        book.setTotalCopies(rs.getInt("totalCopies"));
        book.setAvailableCopies(rs.getInt("availableCopies"));
        book.setBookImage(rs.getBytes("bookImage"));
        return book;
    }
}
