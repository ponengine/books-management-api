package books_management.api.dto.get_all_book.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class GetAllBooksResponse {
    private Integer id;
    private String title;
    private String author;
    private Date publishedDate;

    public GetAllBooksResponse() {
    }

    public GetAllBooksResponse(Integer id, String title, String author, Date publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "GetAllBooksResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedDate=" + publishedDate +
                '}';
    }
}
