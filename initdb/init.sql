CREATE TABLE book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    published_date DATE,
    created_by VARCHAR(100),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

INSERT INTO book (title, author, published_date, created_by)
VALUES
('Clean Code', 'Robert C. Martin', '2008-08-11', 'admin'),
('The Pragmatic Programmer', 'Andy Hunt', '1999-10-30', 'admin');
