CREATE TABLE book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    publishedDate DATE,
    createdBy VARCHAR(100),
    createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    updatedBy VARCHAR(100),
    updatedDate DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    isActive BOOLEAN DEFAULT TRUE
);

INSERT INTO book (title, author, publishedDate, createdBy)
VALUES
('Clean Code', 'Robert C. Martin', '2008-08-11', 'admin'),
('The Pragmatic Programmer', 'Andy Hunt', '1999-10-30', 'admin');
