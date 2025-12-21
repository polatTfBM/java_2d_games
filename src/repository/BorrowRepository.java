package repository;

import db.DatabaseConnection;
import model.Borrow;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowRepository {

    public Optional<Borrow> findActiveBorrow(int userId, int bookId) throws SQLException {
        String sql = "SELECT * FROM borrows WHERE userId = ? AND bookId = ? AND returned = FALSE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, bookId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        }
    }

    public Borrow save(Borrow borrow) throws SQLException {
        String sql = "INSERT INTO borrows (userId, bookId, borrowDate, returned) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, borrow.getUserId());
            statement.setInt(2, borrow.getBookId());
            statement.setTimestamp(3, Timestamp.valueOf(borrow.getBorrowDate()));
            statement.setBoolean(4, borrow.isReturned());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                borrow.setId(keys.getInt(1));
            }
            return borrow;
        }
    }

    public Borrow updateReturn(Borrow borrow) throws SQLException {
        String sql = "UPDATE borrows SET returnDate = ?, returned = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (borrow.getReturnDate() != null) {
                statement.setTimestamp(1, Timestamp.valueOf(borrow.getReturnDate()));
            } else {
                statement.setNull(1, Types.TIMESTAMP);
            }
            statement.setBoolean(2, borrow.isReturned());
            statement.setInt(3, borrow.getId());
            statement.executeUpdate();
            return borrow;
        }
    }

    public int countActiveBorrowsForBook(int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM borrows WHERE bookId = ? AND returned = FALSE";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Borrow> findByUser(int userId) throws SQLException {
        List<Borrow> borrows = new ArrayList<>();
        String sql = "SELECT * FROM borrows WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                borrows.add(mapRow(rs));
            }
        }
        return borrows;
    }

    private Borrow mapRow(ResultSet rs) throws SQLException {
        Borrow borrow = new Borrow();
        borrow.setId(rs.getInt("id"));
        borrow.setUserId(rs.getInt("userId"));
        borrow.setBookId(rs.getInt("bookId"));
        Timestamp borrowDate = rs.getTimestamp("borrowDate");
        if (borrowDate != null) {
            borrow.setBorrowDate(borrowDate.toLocalDateTime());
        }
        Timestamp returnDate = rs.getTimestamp("returnDate");
        if (returnDate != null) {
            borrow.setReturnDate(returnDate.toLocalDateTime());
        }
        borrow.setReturned(rs.getBoolean("returned"));
        return borrow;
    }
}
