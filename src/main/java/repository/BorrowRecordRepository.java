package repository;

import db.DBConnection;
import model.BorrowRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for borrow records.
 */
public class BorrowRecordRepository {

    public void createBorrowRecord(BorrowRecord record) {
        String sql = "INSERT INTO borrow_records (user_id, book_id, due_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getUserId());
            ps.setInt(2, record.getBookId());
            ps.setTimestamp(3, Timestamp.valueOf(record.getDueDate()));
            ps.setString(4, record.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    record.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBorrowRecord(BorrowRecord record) {
        String sql = "UPDATE borrow_records SET return_date=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (record.getReturnDate() != null) {
                ps.setTimestamp(1, Timestamp.valueOf(record.getReturnDate()));
            } else {
                ps.setNull(1, Types.TIMESTAMP);
            }
            ps.setString(2, record.getStatus());
            ps.setInt(3, record.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BorrowRecord> findActiveBorrowsByUser(int userId) {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE user_id = ? AND status = ? ORDER BY borrow_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, BorrowRecord.STATUS_BORROWED);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BorrowRecord> findHistoryByUser(int userId) {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM borrow_records WHERE user_id = ? ORDER BY borrow_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BorrowRecord mapRow(ResultSet rs) throws SQLException {
        BorrowRecord br = new BorrowRecord();
        br.setId(rs.getInt("id"));
        br.setUserId(rs.getInt("user_id"));
        br.setBookId(rs.getInt("book_id"));
        Timestamp borrowTs = rs.getTimestamp("borrow_date");
        Timestamp dueTs = rs.getTimestamp("due_date");
        Timestamp returnTs = rs.getTimestamp("return_date");
        if (borrowTs != null) {
            br.setBorrowDate(borrowTs.toLocalDateTime());
        }
        if (dueTs != null) {
            br.setDueDate(dueTs.toLocalDateTime());
        }
        if (returnTs != null) {
            br.setReturnDate(returnTs.toLocalDateTime());
        }
        br.setStatus(rs.getString("status"));
        return br;
    }
}
