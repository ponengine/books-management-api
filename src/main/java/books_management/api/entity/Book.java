package books_management.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "book", uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    private Date publishedDate;

    @Column(length = 100)
    private String createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(length = 100)
    private String updatedBy;

    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

