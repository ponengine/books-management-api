package books_management.api.repository;

import books_management.api.dto.get_all_book.response.GetAllBooksResponse;
import books_management.api.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BooksRepository extends JpaRepository<Books,Integer> {
    @Query(nativeQuery = true,
            value = "SELECT b.id, b.title, b.author, b.published_date " +
                    "FROM books b " +
                    "WHERE b.author = :author"+
                    " ORDER BY b.id DESC")
    Page<GetAllBooksResponse> findByAuthorName(@Param("author") String author, Pageable pageable);

    boolean existsByTitle(String title);
}
