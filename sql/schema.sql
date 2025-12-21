CREATE DATABASE IF NOT EXISTS library_app;
USE library_app;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    totalCopies INT NOT NULL,
    availableCopies INT NOT NULL,
    bookImage LONGBLOB
);

CREATE TABLE IF NOT EXISTS borrows (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    bookId INT NOT NULL,
    borrowDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    returnDate TIMESTAMP NULL,
    returned BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_borrow_user FOREIGN KEY (userId) REFERENCES users(id),
    CONSTRAINT fk_borrow_book FOREIGN KEY (bookId) REFERENCES books(id)
);

INSERT INTO users (username, password, role)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;
