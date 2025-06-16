CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    published_date DATE,
    created_by VARCHAR(100),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN
);
CREATE INDEX idx_books_author ON books(author);
INSERT INTO books (title, author, published_date, created_by)
VALUES
('Clean Code', 'Robert C. Martin', '2008-08-11', 'admin'),
('The Pragmatic Programmer', 'Andy Hunt', '1999-10-30', 'admin'),
('Design Patterns', 'Erich Gamma', '1994-02-01', 'admin'),
('Refactoring', 'Martin Fowler', '1999-07-08', 'admin'),
('Code Complete', 'Steve McConnell', '2004-06-09', 'admin'),
('The Java Programming Language', 'Ken Arnold', '2005-05-27', 'admin'),
('Effective Java', 'Joshua Bloch', '2001-05-28', 'admin'),
('Introduction to Algorithms', 'Thomas H. Cormen', '2009-07-31', 'admin'),
('The Art of Computer Programming', 'Donald E. Knuth', '1997-01-01', 'admin'),
('You Don\'t Know JS', 'Kyle Simpson', '2014-05-08', 'admin');
