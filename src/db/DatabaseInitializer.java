package db;

import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public void initialize() throws SQLException {
        createTables();
        seedAdminUser();
    }

    private void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(100) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "role VARCHAR(20) NOT NULL" +
                ")";

        String bookTable = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(200) NOT NULL," +
                "author VARCHAR(200) NOT NULL," +
                "category VARCHAR(100) NOT NULL," +
                "description TEXT," +
                "totalCopies INT NOT NULL," +
                "availableCopies INT NOT NULL," +
                "bookImage LONGBLOB" +
                ")";

        String borrowTable = "CREATE TABLE IF NOT EXISTS borrows (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "userId INT NOT NULL," +
                "bookId INT NOT NULL," +
                "borrowDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "returnDate TIMESTAMP NULL," +
                "returned BOOLEAN DEFAULT FALSE," +
                "CONSTRAINT fk_borrow_user FOREIGN KEY (userId) REFERENCES users(id)," +
                "CONSTRAINT fk_borrow_book FOREIGN KEY (bookId) REFERENCES books(id)" +
                ")";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(userTable);
            statement.execute(bookTable);
            statement.execute(borrowTable);
        }
    }

    private void seedAdminUser() throws SQLException {
        String checkAdmin = "SELECT id FROM users WHERE username = ?";
        String insertAdmin = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkAdmin)) {
            checkStmt.setString(1, "admin");
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertAdmin)) {
                    insertStmt.setString(1, "admin");
                    insertStmt.setString(2, PasswordUtil.hash("admin123"));
                    insertStmt.setString(3, "ADMIN");
                    insertStmt.executeUpdate();
                }
            }
        }
    }
}
