package com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.book;

import com.flp.ddd.dddlibrary.catalog.domain.book.Isbn;
import com.flp.ddd.dddlibrary.catalog.infrastructure.persistence.copy.CopyEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "book_id")
    private UUID bookId;
    private String title;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CopyEntity> copies;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "isbn"))
    private Isbn isbn;

    public void addCopy(CopyEntity copy) {
        copies.add(copy);
        copy.setBook(this);
    }
}
