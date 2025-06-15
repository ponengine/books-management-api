package books_management.api.repository;

import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book,Integer> {
    @Query(nativeQuery = true,
            value = "SELECT b.id, b.title, b.author, b.publishedDate " +
                    "FROM book b " +
                    "WHERE b.author = :author")
    List<GetAllBooksResponse> findByAuthorName(@Param("author") String author);

    boolean existsByTitle(String title);
}
