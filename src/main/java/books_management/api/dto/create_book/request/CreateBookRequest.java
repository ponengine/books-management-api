package books_management.api.dto.create_book.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateBookRequest {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Author is required")
    private String author;

    private String publishedDate;

    private String createdBy;

    @Override
    public String toString() {
        return "CreateBookRequest{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
